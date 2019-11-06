package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.common.entity.App;

public interface RoleInitializationService {

    /**
     *
     * @param app
     */
  public void initAppRoles(App app);

    /**
     * 初始化该命名空间的 修改和发布权限
     * @param appId
     * @param namespaceName
     * @param operator
     */
  public void initNamespaceRoles(String appId, String namespaceName, String operator);


    /**
     * 初始化portal可提供的所有环境的命名空间 修改和发布角色
     * @param appId
     * @param namespaceName
     * @param operator
     */
  public void initNamespaceEnvRoles(String appId, String namespaceName, String operator);

  public void initNamespaceSpecificEnvRoles(String appId, String namespaceName, String env, String operator);

    /**
     * 初始化创建app的角色
     */
  public void initCreateAppRole();

    /**
     * 初始化管理app的master角色
     * @param appId
     * @param operator
     */
  public void initManageAppMasterRole(String appId, String operator);

}
