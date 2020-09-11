package com.crm.service.impl;

import com.crm.domain.Attendance;
import com.crm.domain.PageResult;
import com.crm.mapper.AttendanceMapper;
import com.crm.query.AttendanceQueryObject;
import com.crm.query.QueryObject;
import com.crm.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceImpl implements IAttendanceService {

    @Autowired
    private AttendanceMapper attendanceDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return attendanceDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Attendance record) {
        return attendanceDao.insert(record);
    }

    @Override
    public Attendance selectByPrimaryKey(Long id) {
        return attendanceDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Attendance> selectAll() {
        return attendanceDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Attendance record) {
        return attendanceDao.updateByPrimaryKey(record);
    }

    @Override
    public Attendance queryForSignInfo(Long empId) {
        Date endTime = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        Date startTime = c.getTime();
        return attendanceDao.queryForSignInfo(empId, startTime, endTime);
    }

    @Override
    public PageResult queryForPage(AttendanceQueryObject queryObject) {
        Long total = attendanceDao.getTotalRows(queryObject);
        if (total == 0)
            return new PageResult();
        List<Attendance> attendanceList = attendanceDao.queryForPage(queryObject);
        return new PageResult(total,attendanceList);
    }
}
