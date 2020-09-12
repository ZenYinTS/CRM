package com.crm.service.impl;

import com.crm.domain.Attendance;
import com.crm.domain.Employee;
import com.crm.domain.Monthattend;
import com.crm.mapper.AttendanceMapper;
import com.crm.mapper.EmployeeMapper;
import com.crm.mapper.MonthattendMapper;
import com.crm.query.MonthAttendQueryObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestGroupBy {
    public static ApplicationContext ioc = new ClassPathXmlApplicationContext("application.xml");

    public static void main(String[] args) throws ParseException {
        AttendanceMapper attendanceDao = ioc.getBean(AttendanceMapper.class);
        MonthattendMapper monthattendDao = ioc.getBean(MonthattendMapper.class);
        EmployeeMapper employeeDao = ioc.getBean(EmployeeMapper.class);
        //1. 获取考勤表的所有员工
        Long[] eids = attendanceDao.queryForEmpId();
        //2. 循环，对考勤表的每个员工获取其打卡的年份与月份
        for (Long eid:eids){
            List<Attendance> attendanceList = attendanceDao.queryByEid(eid);
            //年份-月份的字符串存放在set中
            Set<String> dateString = new HashSet<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (Attendance attendance : attendanceList){
                String date = sdf.format(attendance.getSignintime());
                dateString.add(date);
            }

            //遍历set，获取年份以及对应的月份
            for (String s:dateString){
                String[] ym = s.split("-");
                //年份
                Integer year = Integer.parseInt(ym[0]);
                //月份
                Integer month = Integer.parseInt(ym[1]);
                //在统计表中查找
                MonthAttendQueryObject queryObject = new MonthAttendQueryObject();
                queryObject.setYear(year);
                queryObject.setMonth(month);
                queryObject.setEid(eid);
                List<Monthattend> m = monthattendDao.queryForPage(queryObject);

                Employee e = employeeDao.selectByPrimaryKey(eid);
                Integer monthAttendCount = attendanceDao.countMonthAttend(eid,year,month,null);
                Integer monthLateCount = attendanceDao.countMonthAttend(eid,year,month,Short.parseShort("1"));
                Integer monthLeaveEarlyCount = attendanceDao.countMonthAttend(eid,year,month,Short.parseShort("2"));
                Monthattend monthattend = new Monthattend(null,e,sdf.parse(s),monthAttendCount,monthLateCount,monthLeaveEarlyCount);
                //不存在则添加
                if (m==null || m.size() == 0){
                    monthattendDao.insert(monthattend);
                }else {
                    //存在则更新
                    monthattendDao.updateByPrimaryKey(monthattend);
                }
            }
        }


    }
}
