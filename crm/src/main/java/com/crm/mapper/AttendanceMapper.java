package com.crm.mapper;

import com.crm.domain.Attendance;
import com.crm.query.QueryObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attendance record);

    Attendance selectByPrimaryKey(Long id);

    List<Attendance> selectAll();

    int updateByPrimaryKey(Attendance record);

    Attendance queryForSignInfo(@Param("empId") Long empId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Long getTotalRows(QueryObject queryObject);

    List<Attendance> queryForPage(QueryObject queryObject);
}