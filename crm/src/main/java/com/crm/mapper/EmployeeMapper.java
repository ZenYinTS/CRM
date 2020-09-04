package com.crm.mapper;

import com.crm.domain.Employee;
import com.crm.query.EmployeeQueryObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll();

    int updateByPrimaryKey(Employee record);

    void updateState(Long id);

    List queryForPage(EmployeeQueryObject queryObject);

    Long getTotalForPage(EmployeeQueryObject queryObject);

    Employee getEmpForLogin(@Param("username") String username,@Param("password") String password);

    void insertRelation(@Param("eid") Long eid,@Param("rid") Long rid);

    void deleteRelation(Long eid);

    List<Employee> queryForDept(Long id);

    List<Employee> selectAllSells(Long inchargeId);
}