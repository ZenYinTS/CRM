package com.crm.service;

import com.crm.domain.Guaranteeitem;

import java.util.List;

public interface IGuaranteeItemService {
    int deleteByPrimaryKey(Long id);

    int insert(Guaranteeitem record);

    Guaranteeitem selectByPrimaryKey(Long id);

    List<Guaranteeitem> selectByGid(Long gid);

    List<Guaranteeitem> selectAll();

    int updateByPrimaryKey(Guaranteeitem record);
}
