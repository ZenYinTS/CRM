package com.crm.service.impl;

import com.crm.domain.Customer;
import com.crm.domain.CustomerChart;
import com.crm.domain.PageResult;
import com.crm.mapper.CustomerChartMapper;
import com.crm.query.CustomerChartQueryObject;
import com.crm.service.ICustomerChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerChartImpl implements ICustomerChartService {

    @Autowired
    private CustomerChartMapper customerChartDao;

    @Override
    public PageResult queryForPage(CustomerChartQueryObject queryObject) {
        Long total = customerChartDao.getTotalRows(queryObject);
        if (total == 0){
            return new PageResult();
        }
        List<CustomerChart> chartList = customerChartDao.queryForPage(queryObject);
        return new PageResult(total,chartList);
    }
}
