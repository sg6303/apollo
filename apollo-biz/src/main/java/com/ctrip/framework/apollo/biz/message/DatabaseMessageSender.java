package com.ctrip.framework.apollo.biz.message;

import com.ctrip.framework.apollo.biz.entity.ReleaseMessage;
import com.ctrip.framework.apollo.biz.repository.ReleaseMessageRepository;
import com.ctrip.framework.apollo.core.utils.ApolloThreadFactory;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ctrip.framework.apollo.tracer.spi.Transaction;
import com.google.common.collect.Queues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Component
public class DatabaseMessageSender implements MessageSender {
  private static final Logger logger = LoggerFactory.getLogger(DatabaseMessageSender.class);
  private static final int CLEAN_QUEUE_MAX_SIZE = 100;
  private BlockingQueue<Long> toClean = Queues.newLinkedBlockingQueue(CLEAN_QUEUE_MAX_SIZE);
  private final ExecutorService cleanExecutorService;
  private final AtomicBoolean cleanStopped;

  private final ReleaseMessageRepository releaseMessageRepository;

  public DatabaseMessageSender(final ReleaseMessageRepository releaseMessageRepository) {
    cleanExecutorService = Executors.newSingleThreadExecutor(ApolloThreadFactory.create("DatabaseMessageSender", true));
    cleanStopped = new AtomicBoolean(false);
    this.releaseMessageRepository = releaseMessageRepository;
  }

  @Override
  @Transactional
  public void sendMessage(String message, String channel) {
    logger.info("Sending message {} to channel {}", message, channel);

    // �������� APOLLO_RELEASE_TOPIC
    if (!Objects.equals(channel, Topics.APOLLO_RELEASE_TOPIC)) {
      logger.warn("Channel {} not supported by DatabaseMessageSender!");
      return;
    }

    Tracer.logEvent("Apollo.AdminService.ReleaseMessage", message);
    Transaction transaction = Tracer.newTransaction("Apollo.AdminService", "sendMessage");
    try {
      ReleaseMessage newMessage = releaseMessageRepository.save(new ReleaseMessage(message));

      // ��ӵ����� Message ���С����������������ʧ�ܣ��������ȴ���
      toClean.offer(newMessage.getId());
      transaction.setStatus(Transaction.SUCCESS);
    } catch (Throwable ex) {
      logger.error("Sending message to database failed", ex);
      transaction.setStatus(ex);
      throw ex;
    } finally {
      transaction.complete();
    }
  }

  @PostConstruct
  private void initialize() {
    cleanExecutorService.submit(() -> {
        // ��δֹͣ���������С�
      while (!cleanStopped.get() && !Thread.currentThread().isInterrupted()) {
        try {
            //��ȡ
          Long rm = toClean.poll(1, TimeUnit.SECONDS);
          // ���зǿգ�������ȡ������Ϣ
          if (rm != null) {
            cleanMessage(rm);
          } else {
            TimeUnit.SECONDS.sleep(5);
          }
        } catch (Throwable ex) {
          Tracer.logError(ex);
        }
      }
    });
  }

  private void cleanMessage(Long id) {
    boolean hasMore = true;
    //double check in case the release message is rolled back
    // ��ѯ��Ӧ�� ReleaseMessage ���󣬱����Ѿ�ɾ������Ϊ��DatabaseMessageSender ���ڶ������ִ�С����磺1��Config Service + Admin Service ��
    // 2��N * Config Service ��3��N * Admin Service
    ReleaseMessage releaseMessage = releaseMessageRepository.findById(id).orElse(null);
    if (releaseMessage == null) {
      return;
    }
    while (hasMore && !Thread.currentThread().isInterrupted()) {
      // ��ȡ��ͬ��Ϣ���ݵ� 100 ��������Ϣ
      // ����Ϣ�Ķ��壺�ȵ�ǰ��Ϣ���С�����ȷ��͵�
      List<ReleaseMessage> messages = releaseMessageRepository.findFirst100ByMessageAndIdLessThanOrderByIdAsc(
          releaseMessage.getMessage(), releaseMessage.getId());

      releaseMessageRepository.deleteAll(messages);
      hasMore = messages.size() == 100;

      messages.forEach(toRemove -> Tracer.logEvent(
          String.format("ReleaseMessage.Clean.%s", toRemove.getMessage()), String.valueOf(toRemove.getId())));
    }
  }

  void stopClean() {
    cleanStopped.set(true);
  }
}
