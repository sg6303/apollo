package com.ctrip.framework.apollo.configservice.util;

import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.configservice.service.AppNamespaceServiceWithCache;
import org.springframework.stereotype.Component;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Component
public class NamespaceUtil {

  private final AppNamespaceServiceWithCache appNamespaceServiceWithCache;

  public NamespaceUtil(final AppNamespaceServiceWithCache appNamespaceServiceWithCache) {
    this.appNamespaceServiceWithCache = appNamespaceServiceWithCache;
  }

  /**
   * ȡ�����ռ�����ơ������properties���͵ģ�ȥ�������׺�������Ĳ���ȥ����׺
   * @param namespaceName
   * @return
   */
  public String filterNamespaceName(String namespaceName) {
    if (namespaceName.toLowerCase().endsWith(".properties")) {
      int dotIndex = namespaceName.lastIndexOf(".");
      return namespaceName.substring(0, dotIndex);
    }

    return namespaceName;
  }

  /**
   * ��ȡ�����ռ����ơ�
   * 1.����appId+namespaceȥ�����ȡ��
   * 2.�������ûȡ��������namespace����ȡ�����������ռ�
   * 3.�����û�У����� null
   * @param appId
   * @param namespaceName
   * @return
   */
  public String normalizeNamespace(String appId, String namespaceName) {
    //�ӻ���ȡappId+namespace ����
    AppNamespace appNamespace = appNamespaceServiceWithCache.findByAppIdAndNamespace(appId, namespaceName);
    if (appNamespace != null) {
      return appNamespace.getName();
    }

    appNamespace = appNamespaceServiceWithCache.findPublicNamespaceByName(namespaceName);
    if (appNamespace != null) {
      return appNamespace.getName();
    }

    return namespaceName;
  }
}
