package com.crm.service.impl;

import com.crm.domain.Contract;
import com.crm.domain.PageResult;
import com.crm.mapper.ContractMapper;
import com.crm.query.ContractQueryObject;
import com.crm.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractImpl implements IContractService {

    @Autowired
    private ContractMapper contractDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Contract record) {
        return contractDao.insert(record);
    }

    @Override
    public Contract selectByPrimaryKey(Long id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Contract> selectAll() {
        return contractDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Contract record) {
        return contractDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(ContractQueryObject queryObject) {
        Long total = contractDao.getTotalRows(queryObject);
        if (total == 0)
            return new PageResult();
        List<Contract> ContractList = contractDao.queryForPage(queryObject);
        return new PageResult(total,ContractList);
    }
}
