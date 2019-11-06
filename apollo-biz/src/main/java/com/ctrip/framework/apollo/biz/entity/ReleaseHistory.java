package com.ctrip.framework.apollo.biz.entity;

import com.ctrip.framework.apollo.common.entity.BaseEntity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 发布历史
 * @author Jason Song(song_s@ctrip.com)
 */
@Entity
@Table(name = "ReleaseHistory")
@SQLDelete(sql = "Update ReleaseHistory set isDeleted = 1 where id = ?")
@Where(clause = "isDeleted = 0")
public class ReleaseHistory extends BaseEntity {
  @Column(name = "AppId", nullable = false)
  private String appId;

  @Column(name = "ClusterName", nullable = false)
  private String clusterName;

  @Column(name = "NamespaceName", nullable = false)
  private String namespaceName;

    /**
     * 发布分支名
     */
  @Column(name = "BranchName", nullable = false)
  private String branchName;

  @Column(name = "ReleaseId")
  private long releaseId;

    /**
     * 前一次发布的ReleaseId
     */
  @Column(name = "PreviousReleaseId")
  private long previousReleaseId;

    /**
     * 发布类型，0: 普通发布，1: 回滚，2: 灰度发布，3: 灰度规则更新，4: 灰度合并回主分支发布，5: 主分支发布灰度自动发布，6: 主分支回滚灰度自动发布，7: 放弃灰度
     */
  @Column(name = "Operation")
  private int operation;

    /**
     * 发布上下文信息
     */
  @Column(name = "OperationContext", nullable = false)
  private String operationContext;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }

  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public long getReleaseId() {
    return releaseId;
  }

  public void setReleaseId(long releaseId) {
    this.releaseId = releaseId;
  }

  public long getPreviousReleaseId() {
    return previousReleaseId;
  }

  public void setPreviousReleaseId(long previousReleaseId) {
    this.previousReleaseId = previousReleaseId;
  }

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }

  public String getOperationContext() {
    return operationContext;
  }

  public void setOperationContext(String operationContext) {
    this.operationContext = operationContext;
  }

  public String toString() {
    return toStringHelper().add("appId", appId).add("clusterName", clusterName)
        .add("namespaceName", namespaceName).add("branchName", branchName)
        .add("releaseId", releaseId).add("previousReleaseId", previousReleaseId)
        .add("operation", operation).toString();
  }
}
