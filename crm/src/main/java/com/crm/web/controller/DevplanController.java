package com.crm.web.controller;

import com.crm.domain.Devplan;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.DevplanQueryObject;
import com.crm.service.IDevplanService;
import com.crm.util.AjaxResult;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DevplanController {

    @Autowired
    private IDevplanService devplanService;

    @RequestMapping("/devplan")
    public String list(Boolean type, Model model){
        model.addAttribute("type",type);
        return "devplan";
    }

    @ResponseBody
    @RequestMapping("/devplan_list")
    public PageResult queryForPage(Boolean type,DevplanQueryObject queryObject){
        queryObject.setUserId(((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId());
        queryObject.setType(type);
        return devplanService.queryForPage(queryObject);
    }

    @ResponseBody
    @RequestMapping("/devplan_save")
    public AjaxResult save(Devplan devplan){
        AjaxResult result = null;
        try {
            devplanService.insert(devplan);
            result = new AjaxResult(true,"添加成功！");
        }catch (Exception e){
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/devplan_update")
    public AjaxResult update(Devplan devplan){
        AjaxResult result = null;
        try {
            devplanService.updateByPrimaryKey(devplan);
            result = new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/devplan_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            devplanService.deleteByPrimaryKey(id);
            result = new AjaxResult(true,"删除成功！");
        }catch (Exception e){
            result = new AjaxResult("删除失败，请联系管理员！");
        }
        return result;
    }
}
