package com.crm.service.impl;

import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.domain.Role;
import com.crm.mapper.EmployeeMapper;
import com.crm.query.EmployeeQueryObject;
import com.crm.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeImpl implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeDao;

    @Override
    public Employee selectByUsername(String username) {
        return employeeDao.selectByUsername(username);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        employeeDao.deleteRelation(id);
        int effectCount =  employeeDao.deleteByPrimaryKey(id);
        return effectCount;
    }

    @Override
    public int insert(Employee record) {
        int effectCount = employeeDao.insert(record);
        if (record.getRoles()!=null) {
            for (Role role : record.getRoles()) {
                employeeDao.insertRelation(record.getId(), role.getId());
            }
        }
        return effectCount;
    }

    @Override
    public Employee selectByPrimaryKey(Long id) {
        return employeeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Employee> selectAll() {
        return employeeDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Employee record) {
        int effectCount = employeeDao.updateByPrimaryKey(record);
        //中间表的处理
        //删除原有的关系
        employeeDao.deleteRelation(record.getId());
        //添加新的关系
        List<Role> roles = record.getRoles()==null?new ArrayList<Role>():record.getRoles();
        for (Role role:roles){
            employeeDao.insertRelation(record.getId(),role.getId());
        }
        return effectCount;
    }

    @Override
    public Employee getEmpForLogin(String username, String password) {
       return employeeDao.getEmpForLogin(username,password);
    }

    @Override
    public PageResult queryForPage(EmployeeQueryObject queryObject) {
        //查询总的记录数
        Long total = employeeDao.getTotalForPage(queryObject);
        if (total==0)
            return new PageResult();
        //查询总的结果集
        List result = employeeDao.queryForPage(queryObject);
        return new PageResult(total,result);
    }

    @Override
    public void updateState(Long id) {
        employeeDao.updateState(id);
    }

    @Override
    public List<Employee> queryForDept(Long id) {
        return employeeDao.queryForDept(id);
    }

    @Override
    public List<Employee> selectAllSells(Long inchargeId) {
        return employeeDao.selectAllSells(inchargeId);
    }

    @Override
    public String[] getFEEmails() {
        return employeeDao.getFEEmails();
    }
}
