package com.crm.mapper;

import com.crm.domain.Department;
import com.crm.domain.PageResult;
import com.crm.query.DepartmentQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    Department selectByPrimaryKey(Long id);

    List<Department> selectAll();

    int updateByPrimaryKey(Department record);

    List<Department> queryForEmp();

    List<Department> queryForDept(Long id);

    List<Department> queryForPage(DepartmentQueryObject queryObject);

    void updateState(Long id);

    Long getTotalForPage(DepartmentQueryObject queryObject);
}