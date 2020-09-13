package com.crm.service.impl;

import com.crm.domain.ContractChart;
import com.crm.domain.PageResult;
import com.crm.mapper.ContractChartMapper;
import com.crm.query.ContractChartQueryObject;
import com.crm.service.IContractChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractChartImpl implements IContractChartService {

    @Autowired
    private ContractChartMapper contractChartDao;

    @Override
    public PageResult queryForPage(ContractChartQueryObject queryObject) {
        Long total = contractChartDao.getTotalRows(queryObject);
        if (total==0)
            return new PageResult();
        List<ContractChart> chartList = contractChartDao.queryForPage(queryObject);
        return new PageResult(total,chartList);
    }
}
