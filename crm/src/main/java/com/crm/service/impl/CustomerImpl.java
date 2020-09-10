package com.crm.service.impl;

import com.crm.domain.Customer;
import com.crm.domain.Dictdetail;
import com.crm.domain.PageResult;
import com.crm.mapper.CustomerMapper;
import com.crm.query.CustomerQueryObject;
import com.crm.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerImpl implements ICustomerService {

    @Autowired
    private CustomerMapper customerDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return customerDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Customer record) {
        return customerDao.insert(record);
    }

    @Override
    public Customer selectByPrimaryKey(Long id) {
        return customerDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Customer> selectAll() {
        return customerDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Customer record) {
        return customerDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(CustomerQueryObject queryObject) {
        //查询总的记录数
        Long total = customerDao.getTotalForPage(queryObject);
        if (total==0)
            return new PageResult();
        //查询总的结果集
        List result = customerDao.queryForPage(queryObject);
        return new PageResult(total,result);
    }

    @Override
    public List<Customer> selectAllFormal() {
        return customerDao.selectAllFormal();
    }

    @Override
    public int updateStatus(Long id, int i) {
        return customerDao.updateStatus(id,i);
    }

    @Override
    public List<Customer> queryForPageOnly(CustomerQueryObject queryObject) {
        return customerDao.queryForPage(queryObject);
    }

    @Override
    public List<Customer> listContractCustomer() {
        return customerDao.listContractCustomer();
    }
}
