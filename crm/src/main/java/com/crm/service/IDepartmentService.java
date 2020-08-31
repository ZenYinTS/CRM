package com.crm.service;

import com.crm.domain.Department;
import com.crm.domain.PageResult;
import com.crm.query.DepartmentQueryObject;

import java.util.List;

public interface IDepartmentService {
    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    Department selectByPrimaryKey(Long id);

    List<Department> selectAll();

    int updateByPrimaryKey(Department record);

    List<Department> queryForEmp();

    List<Department> queryForDept(Long id);

    PageResult queryForPage(DepartmentQueryObject queryObject);

    void updateState(Long id);
}
