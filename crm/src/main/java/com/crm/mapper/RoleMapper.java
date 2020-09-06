package com.crm.mapper;

import com.crm.domain.Role;
import com.crm.query.QueryObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    //可以在role中进行中间表的管理
    int insertRelation(@Param("rid") Long rid,@Param("pid") Long pid);

    Long queryPageCount(QueryObject qo);

    List<Role> queryPage(QueryObject qo);

    void deletePermissionByRid(Long rid);

    List<Long> queryByEid(Long eid);

    List<Role> getRolesByEid(Long id);
}