package com.crm.mapper;

import com.crm.domain.ContractChart;
import com.crm.query.ContractChartQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractChartMapper {
    Long getTotalRows(ContractChartQueryObject queryObject);

    List<ContractChart> queryForPage(ContractChartQueryObject queryObject);
}
