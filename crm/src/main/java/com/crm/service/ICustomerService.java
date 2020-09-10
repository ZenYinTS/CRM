package com.crm.service;

import com.crm.domain.Customer;
import com.crm.domain.Dictdetail;
import com.crm.domain.PageResult;
import com.crm.query.CustomerQueryObject;

import java.util.List;

public interface ICustomerService {

    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    Customer selectByPrimaryKey(Long id);

    List<Customer> selectAll();

    int updateByPrimaryKey(Customer record);

    PageResult queryForPage(CustomerQueryObject queryObject);

    int updateStatus(Long id, int i);

    List<Customer> selectAllFormal();

    List<Customer> queryForPageOnly(CustomerQueryObject queryObject);

    List<Customer> listContractCustomer();
}
