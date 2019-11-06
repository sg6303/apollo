package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.common.entity.App;

public interface RoleInitializationService {

    /**
     *
     * @param app
     */
  public void initAppRoles(App app);

    /**
     * ��ʼ���������ռ�� �޸ĺͷ���Ȩ��
     * @param appId
     * @param namespaceName
     * @param operator
     */
  public void initNamespaceRoles(String appId, String namespaceName, String operator);


    /**
     * ��ʼ��portal���ṩ�����л����������ռ� �޸ĺͷ�����ɫ
     * @param appId
     * @param namespaceName
     * @param operator
     */
  public void initNamespaceEnvRoles(String appId, String namespaceName, String operator);

  public void initNamespaceSpecificEnvRoles(String appId, String namespaceName, String env, String operator);

    /**
     * ��ʼ������app�Ľ�ɫ
     */
  public void initCreateAppRole();

    /**
     * ��ʼ������app��master��ɫ
     * @param appId
     * @param operator
     */
  public void initManageAppMasterRole(String appId, String operator);

}
