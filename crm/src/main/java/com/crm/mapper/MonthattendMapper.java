package com.crm.mapper;

import com.crm.domain.Monthattend;
import com.crm.query.MonthAttendQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthattendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Monthattend record);

    Monthattend selectByPrimaryKey(Long id);

    List<Monthattend> selectAll();

    int updateByPrimaryKey(Monthattend record);

    Long getTotalRows(MonthAttendQueryObject queryObject);

    List<Monthattend> queryForPage(MonthAttendQueryObject queryObject);
}