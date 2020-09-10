package com.crm.web.controller;

import com.crm.domain.*;
import com.crm.service.IGuaranteeItemService;
import com.crm.service.IGuaranteeService;
import com.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GuaranteeItemController {

    @Autowired
    private IGuaranteeItemService guaranteeItemService;

    @Autowired
    private IGuaranteeService guaranteeService;

    @ResponseBody
    @RequestMapping("/guarantee_item_list")
    public List<Guaranteeitem> list(Long gid){
        return guaranteeItemService.selectByGid(gid);
    }

    @ResponseBody
    @RequestMapping("/guarantee_item_save")
    public AjaxResult save(Guaranteeitem item,Long gid){
        AjaxResult result = null;
        try {
            item.setGuarantee(guaranteeService.selectByPrimaryKey(gid));
            guaranteeItemService.insert(item);
            result = new AjaxResult(true,"添加成功！");
        }catch (Exception e){
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/guarantee_item_update")
    public AjaxResult update(Guaranteeitem item){
        AjaxResult result = null;
        try {
            int i = guaranteeItemService.updateByPrimaryKey(item);
            result = new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/guarantee_item_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            guaranteeItemService.deleteByPrimaryKey(id);
            result = new AjaxResult(true,"删除成功！");
        }catch (Exception e){
            result = new AjaxResult("删除失败，请联系管理员！");
        }
        return result;
    }
}
