package com.crm.mapper;

import com.crm.domain.Dictdetail;
import com.crm.query.QueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictdetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Dictdetail record);

    Dictdetail selectByPrimaryKey(Long id);

    List<Dictdetail> selectAll();

    int updateByPrimaryKey(Dictdetail record);

    List<Dictdetail> selectByPid(Long pid);
}