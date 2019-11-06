package com.ctrip.framework.apollo.common.constants;

/**
 * 发布类型，0: 普通发布，1: 回滚，2: 灰度发布，3: 灰度规则更新，4: 灰度合并回主分支发布，5: 主分支发布灰度自动发布，6: 主分支回滚灰度自动发布，7: 放弃灰度
 * @author Jason Song(song_s@ctrip.com)
 */
public interface ReleaseOperation {
  int NORMAL_RELEASE = 0;
  int ROLLBACK = 1;
  int GRAY_RELEASE = 2;
  int APPLY_GRAY_RULES = 3;
  int GRAY_RELEASE_MERGE_TO_MASTER = 4;
  int MASTER_NORMAL_RELEASE_MERGE_TO_GRAY = 5;
  int MATER_ROLLBACK_MERGE_TO_GRAY = 6;
  int ABANDON_GRAY_RELEASE = 7;
  int GRAY_RELEASE_DELETED_AFTER_MERGE = 8;
}
