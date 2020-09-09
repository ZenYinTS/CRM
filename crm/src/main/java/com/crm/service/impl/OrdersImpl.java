package com.crm.service.impl;

import com.crm.domain.Orders;
import com.crm.domain.PageResult;
import com.crm.mapper.OrdersMapper;
import com.crm.query.OrdersQueryObject;
import com.crm.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersImpl implements IOrdersService {

    @Autowired
    private OrdersMapper ordersDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ordersDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Orders record) {
        return ordersDao.insert(record);
    }

    @Override
    public Orders selectByPrimaryKey(Long id) {
        return ordersDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Orders> selectAll() {
        return ordersDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Orders record) {
        return ordersDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(OrdersQueryObject queryObject) {
        Long total = ordersDao.getTotalRows(queryObject);
        if (total == 0)
            return new PageResult();
        List<Orders> ordersList = ordersDao.queryForPage(queryObject);
        return new PageResult(total,ordersList);
    }
}
