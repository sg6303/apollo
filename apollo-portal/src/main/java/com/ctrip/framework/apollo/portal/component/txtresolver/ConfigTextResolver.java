package com.ctrip.framework.apollo.portal.component.txtresolver;

import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ItemDTO;

import java.util.List;

/**
 * users can modify config in text mode.so need resolve text.
 *
 * �����ı��������ӿ�
 */
public interface ConfigTextResolver {

    /**
     * �����ı������� ItemChangeSets ����
     *
     * @param namespaceId Namespace ���
     * @param configText �����ı�
     * @param baseItems �Ѵ��ڵ� ItemDTO ��
     * @return ItemChangeSets ����
     */
  ItemChangeSets resolve(long namespaceId, String configText, List<ItemDTO> baseItems);

}
