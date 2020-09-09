package com.crm.mapper;

import com.crm.domain.Orders;
import com.crm.query.OrdersQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Orders record);

    Orders selectByPrimaryKey(Long id);

    List<Orders> selectAll();

    int updateByPrimaryKey(Orders record);

    Long getTotalRows(OrdersQueryObject queryObject);

    List<Orders> queryForPage(OrdersQueryObject queryObject);
}