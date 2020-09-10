package com.crm.mapper;

import com.crm.domain.Salarytable;
import com.crm.query.SalaryQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalarytableMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Salarytable record);

    Salarytable selectByPrimaryKey(Long id);

    List<Salarytable> selectAll();

    int updateByPrimaryKey(Salarytable record);

    Long getTotalRows(SalaryQueryObject queryObject);

    List<Salarytable> queryForPage(SalaryQueryObject queryObject);
}