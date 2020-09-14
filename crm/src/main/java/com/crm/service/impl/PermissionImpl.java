package com.crm.service.impl;

import com.crm.domain.Permission;
import com.crm.domain.PageResult;
import com.crm.mapper.PermissionMapper;
import com.crm.query.PermissionQueryObject;
import com.crm.query.QueryObject;
import com.crm.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionImpl implements IPermissionService {
    @Autowired
    private PermissionMapper permissionDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return permissionDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Permission record) {
        return permissionDao.insert(record);
    }

    @Override
    public Permission selectByPrimaryKey(Long id) {
        return permissionDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Permission> selectAll() {
        return permissionDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Permission record) {
        return permissionDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(PermissionQueryObject queryObject) {
        //查询总的记录数
        Long total = permissionDao.queryPageCount(queryObject);
        if (total==0) {
            PageResult result = new PageResult();
            result.setRows(new ArrayList());
            result.setTotal(0l);
            return result;
        }
        //查询总的结果集
        List result = permissionDao.queryPage(queryObject);
        return new PageResult(total,result);
    }

    @Override
    public List<String> queryPermissionByEid(Long eid) {
        return permissionDao.queryPermissionByEid(eid);
    }

    @Override
    public void deleteRolePermission(Long id) {
        permissionDao.deleteRolePermission(id);
    }
}
