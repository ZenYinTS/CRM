package com.crm.util;

import com.crm.domain.Employee;
import com.crm.domain.Menu;
import com.crm.domain.Permission;
import com.crm.service.IPermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class PermissionUtils {
    //被static修饰变量，是部署于任何实例化的对象拥有，
    // spring的依赖注入只能在对象层级上进行依赖注入，所以不能直接使用@Autowired标签进行注入。
    private static IPermissionService permissionService;

    //系统菜单权限控制
    //查询是否具有菜单权限，没有就删除
    public static void checkMenuPermission(List<Menu> menus){
        Menu menu;
        //先遍历第一层，最外层的目录
        //由于要删除集合中的元素，可以有两种操作：使用迭代器删除或者倒着删，这里采用倒着删【正着删会导致删除后元素下标指定的元素改变】
        for (int i = menus.size()-1;i>=0;i--){
            menu = menus.get(i);
            //如果权限表达式为空，就认为该菜单项不需要进行权限控制，如果不为空就进行权限控制
            if (StringUtils.isNotBlank(menu.getFunction())){
                //查看用户是否具有该权限
                //不具有则删除
                if (!checkPermission(menu.getFunction())){
                    menus.remove(i);
                }
            }

            //查询子节点，递归
            if (menu.getChildren()!=null&&menu.getChildren().size()>0){
                checkMenuPermission(menu.getChildren());
            }
        }
    }

    //解决方法： 在该变量的set方法上添加@Autowired注解，再将变量注入
    @Autowired
    public void setPermissionService(IPermissionService permissionService){
        this.permissionService = permissionService;
    }
    public static Boolean checkPermission(String function) {
        //超级管理员，直接放行
        HttpSession session = UserContext.get().getSession();
        Employee emp = (Employee)session.getAttribute(UserContext.USERINSESSION);
        if (emp.getAdmin())
            return true;

        //获取数据库中的所有权限，存入集合中
        if (CommonUtils.allPermissions.size()==0){
            List<Permission> permissions = permissionService.selectAll();
            for (Permission permission:permissions){
                CommonUtils.allPermissions.add(permission.getResource());
            }
        }

        //判断表达式是否需要权限控制
        //如果function权限表达式存在数据库的权限集合中，说明需要进行权限控制
        if (CommonUtils.allPermissions.contains(function)){
            //首先获取用户的权限，从session中获取
            List<String> userPermissions = (List<String>) session.getAttribute(UserContext.PERMISSIONINSESSION);

            if (userPermissions == null)
                return false;

            //完全匹配
            if (userPermissions.contains(function)){
                return true;
            }else{
                //All权限匹配
                String allPermission = function.split(":")[0]+":ALL";
                if (userPermissions.contains(allPermission)){
                    return true;
                }else
                    return false;
            }
        }else
            return true;
    }
}
