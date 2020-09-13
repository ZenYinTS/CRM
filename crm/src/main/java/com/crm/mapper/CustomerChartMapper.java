package com.crm.mapper;

import com.crm.domain.CustomerChart;
import com.crm.query.CustomerChartQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerChartMapper {
    Long getTotalRows(CustomerChartQueryObject queryObject);

    List<CustomerChart> queryForPage(CustomerChartQueryObject queryObject);
}
