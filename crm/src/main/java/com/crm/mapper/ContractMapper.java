package com.crm.mapper;

import com.crm.domain.Contract;
import com.crm.query.ContractQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Contract record);

    Contract selectByPrimaryKey(Long id);

    List<Contract> selectAll();

    int updateByPrimaryKey(Contract record);

    Long getTotalRows(ContractQueryObject queryObject);

    List<Contract> queryForPage(ContractQueryObject queryObject);
}