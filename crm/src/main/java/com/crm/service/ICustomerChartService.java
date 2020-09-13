package com.crm.service;

import com.crm.domain.PageResult;
import com.crm.query.CustomerChartQueryObject;

public interface ICustomerChartService {
    PageResult queryForPage(CustomerChartQueryObject queryObject);
}
