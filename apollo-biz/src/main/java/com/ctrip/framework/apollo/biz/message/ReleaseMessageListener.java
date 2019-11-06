package com.ctrip.framework.apollo.biz.message;

import com.ctrip.framework.apollo.biz.entity.ReleaseMessage;

/**
 *  ReleaseMessage ¼àÌıÆ÷½Ó¿Ú
 * @author Jason Song(song_s@ctrip.com)
 */
public interface ReleaseMessageListener {
  void handleMessage(ReleaseMessage message, String channel);
}
