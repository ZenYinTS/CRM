package com.crm.service;

import com.crm.domain.PageResult;
import com.crm.domain.Salarytable;
import com.crm.query.SalaryQueryObject;

import java.util.List;

public interface ISalaryService {
    int deleteByPrimaryKey(Long id);

    int insert(Salarytable record);

    Salarytable selectByPrimaryKey(Long id);

    List<Salarytable> selectAll();

    int updateByPrimaryKey(Salarytable record);

    PageResult queryForPage(SalaryQueryObject queryObject);
}
