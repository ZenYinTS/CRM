package com.crm.service;

import com.crm.domain.Monthattend;
import com.crm.domain.PageResult;
import com.crm.query.MonthAttendQueryObject;

import java.util.List;

public interface IMonthAttendService {
    int deleteByPrimaryKey(Long id);

    int insert(Monthattend record);

    Monthattend selectByPrimaryKey(Long id);

    List<Monthattend> selectAll();

    int updateByPrimaryKey(Monthattend record);

    PageResult queryForPage(MonthAttendQueryObject queryObject);
}
