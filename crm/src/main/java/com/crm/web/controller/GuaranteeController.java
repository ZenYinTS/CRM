package com.crm.web.controller;

import com.crm.domain.Guarantee;
import com.crm.domain.PageResult;
import com.crm.query.GuaranteeQueryObject;
import com.crm.service.IGuaranteeService;
import com.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
public class GuaranteeController {

    @Autowired
    private IGuaranteeService guaranteeService;

    @RequestMapping("/guarantee")
    public String list(){
        return "guarantee";
    }

    @ResponseBody
    @RequestMapping("/guarantee_list")
    public PageResult queryForPage(GuaranteeQueryObject queryObject){
        return guaranteeService.queryForPage(queryObject);
    }

    @ResponseBody
    @RequestMapping("/guarantee_save")
    public AjaxResult save(Guarantee guarantee){
        AjaxResult result = null;
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.setTime(now);
        c.add(Calendar.YEAR,1);
        guarantee.setDuetime(c.getTime());
        guarantee.setSn(UUID.randomUUID().toString());
        try {
            guaranteeService.insert(guarantee);
            result = new AjaxResult(true,"添加成功！");
        }catch (Exception e){
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/guarantee_update")
    public AjaxResult update(Guarantee guarantee){
        AjaxResult result = null;
        try {
            guaranteeService.updateByPrimaryKey(guarantee);
            result = new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/guarantee_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            guaranteeService.deleteByPrimaryKey(id);
            result = new AjaxResult(true,"删除成功！");
        }catch (Exception e){
            result = new AjaxResult("删除失败，请联系管理员！");
        }
        return result;
    }
}
