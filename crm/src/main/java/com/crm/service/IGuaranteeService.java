package com.crm.service;

import com.crm.domain.Guarantee;
import com.crm.domain.PageResult;
import com.crm.query.GuaranteeQueryObject;

import java.util.List;

public interface IGuaranteeService {
    int deleteByPrimaryKey(Long id);

    int insert(Guarantee record);

    Guarantee selectByPrimaryKey(Long id);

    List<Guarantee> selectAll();

    int updateByPrimaryKey(Guarantee record);

    PageResult queryForPage(GuaranteeQueryObject queryObject);
}
