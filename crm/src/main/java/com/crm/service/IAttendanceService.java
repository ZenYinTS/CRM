package com.crm.service;

import com.crm.domain.Attendance;
import com.crm.domain.PageResult;
import com.crm.query.AttendanceQueryObject;
import com.crm.query.QueryObject;

import java.util.Date;
import java.util.List;

public interface IAttendanceService {
    int deleteByPrimaryKey(Long id);

    int insert(Attendance record);

    Attendance selectByPrimaryKey(Long id);

    List<Attendance> selectAll();

    int updateByPrimaryKey(Attendance record);

    Attendance queryForSignInfo(Long empId);

    PageResult queryForPage(AttendanceQueryObject queryObject);
}
