package com.crm.web.controller;

import com.crm.domain.Employee;
import com.crm.domain.Menu;
import com.crm.domain.Permission;
import com.crm.domain.Role;
import com.crm.service.IEmployeeService;
import com.crm.service.IMenuService;
import com.crm.service.IPermissionService;
import com.crm.service.IRoleService;
import com.crm.util.AjaxResult;
import com.crm.util.PermissionUtils;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    @ResponseBody
    @RequestMapping(value="/login")
    public AjaxResult login(String username, String password,
                            HttpServletRequest request){
        AjaxResult result = null;

        //将request绑定到线程中
        UserContext.set(request);

        Employee employee = employeeService.getEmpForLogin(username, password);

        //验证账号密码是否正确
        if (employee!=null){
            if (!employee.getState()){
                //员工离职，则无法登录
                result = new AjaxResult("员工已离职，无法登陆！");
            }else {
                request.getSession().setAttribute(UserContext.USERINSESSION, employee);

                //登录成功后获取用户的权限，并存入session中
                //获取用户权限的resource集合
                List<String> userPermission = permissionService.queryPermissionByEid(employee.getId());
                //存入session中
                request.getSession().setAttribute(UserContext.PERMISSIONINSESSION, userPermission);

                //获取用户角色集合，存入session中
                List<Role> userRoles = roleService.getRolesByEid(employee.getId());
                //存入session中
                request.getSession().setAttribute(UserContext.ROLEINSESSION, userRoles);

                //从数据库中获取菜单信息，并存入session中
                List<Menu> menus = menuService.queryForRoot();
                //传入的是引用，所以会对值造成影响
                PermissionUtils.checkMenuPermission(menus);
                request.getSession().setAttribute(UserContext.MENUINSESSION,menus);
                result = new AjaxResult(true, "登陆成功！");
            }
        }else {
            result = new AjaxResult("账号密码有误！");
        }

        return result;
    }
}
