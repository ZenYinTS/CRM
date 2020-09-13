package com.crm.service;

import com.crm.domain.PageResult;
import com.crm.query.ContractChartQueryObject;

public interface IContractChartService {
    PageResult queryForPage(ContractChartQueryObject queryObject);
}
