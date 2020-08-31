package com.crm.service;

import com.crm.domain.Dictdetail;

import java.util.List;

public interface IDictdetailService {
    int deleteByPrimaryKey(Long id);

    int insert(Dictdetail record);

    Dictdetail selectByPrimaryKey(Long id);

    List<Dictdetail> selectAll();

    int updateByPrimaryKey(Dictdetail record);

    List<Dictdetail> selectByPid(Long pid);
}
