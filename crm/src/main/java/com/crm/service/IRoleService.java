package com.crm.service;

import com.crm.domain.PageResult;
import com.crm.domain.Role;
import com.crm.query.RoleQueryObject;

import java.util.List;

public interface IRoleService {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    PageResult queryForPage(RoleQueryObject queryObject);

    List<Long> queryByEid(Long eid);

    List<Role> getRolesByEid(Long id);
}
