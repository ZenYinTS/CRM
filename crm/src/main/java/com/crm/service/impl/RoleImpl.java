package com.crm.service.impl;

import com.crm.domain.PageResult;
import com.crm.domain.Permission;
import com.crm.domain.Role;
import com.crm.mapper.RoleMapper;
import com.crm.query.RoleQueryObject;
import com.crm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleImpl implements IRoleService {
    @Autowired
    private RoleMapper roleDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        //先删除关系
        roleDao.deletePermissionByRid(id);
        //后删除角色
        int effectCount = roleDao.deleteByPrimaryKey(id);
        return effectCount;
    }

    @Override
    public int insert(Role record) {
        //role表的插入操作
        int effectCount = roleDao.insert(record);

        //中间表的插入操作
        if (record.getPermissions()!=null) {
            for (Permission p : record.getPermissions()) {
                roleDao.insertRelation(record.getId(), p.getId());
            }
        }
        return effectCount;
    }

    @Override
    public Role selectByPrimaryKey(Long id) {
        return roleDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Role> selectAll() {
        return roleDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Role record) {
        int effectRow =  roleDao.updateByPrimaryKey(record);
        //中间表的处理
        //先删除以前的关系
        roleDao.deletePermissionByRid(record.getId());
        //添加新的关系
        for (Permission p : record.getPermissions()){
            roleDao.insertRelation(record.getId(),p.getId());
        }
        return effectRow;
    }

    @Override
    public List<Role> getRolesByEid(Long id) {
        return roleDao.getRolesByEid(id);
    }

    @Override
    public PageResult queryForPage(RoleQueryObject queryObject) {
        Long count = roleDao.queryPageCount(queryObject);
        if (count == 0)
            return new PageResult();
        List<Role> roles = roleDao.queryPage(queryObject);
        return new PageResult(count,roles);
    }

    @Override
    public List<Long> queryByEid(Long eid) {
        return roleDao.queryByEid(eid);
    }
}
