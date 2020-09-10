package com.crm.mapper;

import com.crm.domain.Guarantee;
import com.crm.query.GuaranteeQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuaranteeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Guarantee record);

    Guarantee selectByPrimaryKey(Long id);

    List<Guarantee> selectAll();

    int updateByPrimaryKey(Guarantee record);

    Long getTotalRows(GuaranteeQueryObject queryObject);

    List<Guarantee> queryForPage(GuaranteeQueryObject queryObject);
}