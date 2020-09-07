package com.crm.mapper;

import com.crm.domain.Devplan;
import com.crm.query.DevplanQueryObject;

import java.util.List;

public interface DevplanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Devplan record);

    Devplan selectByPrimaryKey(Long id);

    List<Devplan> selectAll();

    int updateByPrimaryKey(Devplan record);

    List<Devplan> queryForPage(DevplanQueryObject queryObject);

    Long getTotalForPage(DevplanQueryObject queryObject);
}