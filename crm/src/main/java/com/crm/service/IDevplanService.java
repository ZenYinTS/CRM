package com.crm.service;

import com.crm.domain.Devplan;
import com.crm.domain.PageResult;
import com.crm.query.DevplanQueryObject;

import java.util.List;

public interface IDevplanService {
    int deleteByPrimaryKey(Long id);

    int insert(Devplan record);

    Devplan selectByPrimaryKey(Long id);

    List<Devplan> selectAll();

    int updateByPrimaryKey(Devplan record);

    PageResult queryForPage(DevplanQueryObject queryObject);
}
