package com.crm.mapper;

import com.crm.domain.Guaranteeitem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuaranteeitemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Guaranteeitem record);

    Guaranteeitem selectByPrimaryKey(Long id);

    List<Guaranteeitem>  selectByGid(Long gid);

    List<Guaranteeitem> selectAll();

    int updateByPrimaryKey(Guaranteeitem record);
}