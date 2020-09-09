package com.crm.service;

import com.crm.domain.Contract;
import com.crm.domain.PageResult;
import com.crm.query.ContractQueryObject;

import java.util.List;

public interface IContractService {
    int deleteByPrimaryKey(Long id);

    int insert(Contract record);

    Contract selectByPrimaryKey(Long id);

    List<Contract> selectAll();

    int updateByPrimaryKey(Contract record);

    PageResult queryForPage(ContractQueryObject queryObject);
}
