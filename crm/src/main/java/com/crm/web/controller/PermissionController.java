package com.crm.web.controller;

import com.crm.domain.PageResult;
import com.crm.domain.Permission;
import com.crm.query.PermissionQueryObject;
import com.crm.service.IPermissionService;
import com.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @RequestMapping("/permission")
    public String list(){
        return "permission";
    }

    @ResponseBody
    @RequestMapping("/permission_list")
    public PageResult list(PermissionQueryObject qo){
        PageResult result = null;
        result = permissionService.queryForPage(qo);
        return result;
    }

    @ResponseBody
    @RequestMapping("/permissions_queryByRid")
    public PageResult permissionsQueryByRid(PermissionQueryObject qo){
        //请求中携带了参数rid，在PermissionQueryObject中添加rid字段获取参数
        PageResult result = null;
        result = permissionService.queryForPage(qo);
        return result;
    }

    @ResponseBody
    @RequestMapping("/permission_save")
    public AjaxResult save(@Valid Permission record, BindingResult result){
        AjaxResult ajaxResult = null;
        if (result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            StringBuffer errors = new StringBuffer();
            for (int i = 0; i < fieldErrors.size(); i++) {
                String msg = fieldErrors.get(i).toString();
                int index = msg.lastIndexOf("default message")+"default message".length();
                errors.append(msg.substring(index+1)+"\n");
            }
            ajaxResult = new AjaxResult(new String(errors));
        }else {
            try {
                permissionService.insert(record);
                ajaxResult = new AjaxResult(true, "保存成功！");
            }catch (Exception e){
                ajaxResult = new AjaxResult("保存异常，请检查日志！");
            }
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/permission_update")
    public AjaxResult update(@Valid Permission record, BindingResult result){
        AjaxResult ajaxResult = null;
        if (result.hasErrors()){
            ajaxResult = new AjaxResult(result.toString());
        }else {
            try {
                //更新
                permissionService.updateByPrimaryKey(record);
                ajaxResult = new AjaxResult(true,"更新成功！");
            }catch (Exception e){
                ajaxResult = new AjaxResult("更新异常，请检查日志！");
            }
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("permission_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            //删除操作采用级联删除
            permissionService.deleteRolePermission(id);
            permissionService.deleteByPrimaryKey(id);
            result = new AjaxResult(true,"删除成功！");
        }catch (Exception e){
            result = new AjaxResult("删除失败，请检查日志！");
        }
        return result;
    }
}
