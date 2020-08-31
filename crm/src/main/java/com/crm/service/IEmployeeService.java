package com.crm.service;

import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.EmployeeQueryObject;

import java.util.List;

public interface IEmployeeService {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll();

    int updateByPrimaryKey(Employee record);

    Employee getEmpForLogin(String username,String password);

    PageResult queryForPage(EmployeeQueryObject queryObject);

    void updateState(Long id);

    List<Employee> queryForDept(Long id);
}
