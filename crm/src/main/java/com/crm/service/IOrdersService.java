package com.crm.service;

import com.crm.domain.Orders;
import com.crm.domain.PageResult;
import com.crm.query.OrdersQueryObject;

import java.util.List;

public interface IOrdersService {
    int deleteByPrimaryKey(Long id);

    int insert(Orders record);

    Orders selectByPrimaryKey(Long id);

    List<Orders> selectAll();

    int updateByPrimaryKey(Orders record);

    PageResult queryForPage(OrdersQueryObject queryObject);
}
