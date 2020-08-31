package com.crm.mapper;

import com.crm.domain.Permission;
import com.crm.query.QueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Long id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    //使用父类作为参数，真正运行实际上调用的是子类【多态】
    Long queryPageCount(QueryObject qo);

    List<Permission> queryPage(QueryObject qo);

    List<String> queryPermissionByEid(Long eid);

    void deleteRolePermission(Long id);
}