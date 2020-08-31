package com.crm.web.controller;

import com.crm.domain.Department;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.DepartmentQueryObject;
import com.crm.service.IDepartmentService;
import com.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @RequestMapping("/department")
    public String list(){
        return "department";
    }

    @ResponseBody
    @RequestMapping("/dept_queryForDept")
    //要求的返回数据是对象数组的json格式，则返回List，再使用@ResponseBody进行转化
    public List<Department> queryForDept(Long id){
        List result = null;
        result = departmentService.queryForDept(id);
        return result;
    }

    @ResponseBody
    @RequestMapping("/dept_queryForEmp")
    //要求的返回数据是对象数组的json格式，则返回List，再使用@ResponseBody进行转化
    public List<Department> queryForEmp(){
        List result = null;
        result = departmentService.queryForEmp();
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/dept_list")
    public PageResult deptList(DepartmentQueryObject queryObject){
        PageResult result = null;
        //根据请求的字段获取到分页信息
        result = departmentService.queryForPage(queryObject);
        return result;
    }

    @ResponseBody
    @RequestMapping("/dept_save")
    public AjaxResult save(Department department){
        AjaxResult result = null;
        try {
            //department不在表单中的字段赋值
            department.setState(true);
            //插入
            departmentService.insert(department);
            result = new AjaxResult(true,"保存成功！");
        }catch (Exception e){
            result = new AjaxResult("保存异常，请检查日志！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/dept_update")
    public AjaxResult update(Department department){
        AjaxResult result = null;
        try {
            //更新
            departmentService.updateByPrimaryKey(department);
            result = new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新异常，请检查日志！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("dept_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            //删除操作采用软删除，只是修改在职状态
            departmentService.updateState(id);
            result = new AjaxResult(true,"停用成功！");
        }catch (Exception e){
            result = new AjaxResult("停用失败，请检查日志！");
        }
        return result;
    }

}
