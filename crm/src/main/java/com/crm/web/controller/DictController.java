package com.crm.web.controller;

import com.crm.domain.Dictdetail;
import com.crm.service.IDictdetailService;
import com.crm.service.IDictcatalogService;
import com.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class DictController {

    @Autowired
    private IDictdetailService dictdetailService;

    @Autowired
    private IDictcatalogService dictcatalogService;


    @RequestMapping(value="/dictionary")
    public String list(){
        return "dict";
    }

    @ResponseBody
    @RequestMapping(value="/detail_list")
    public List detailList(Long pid){
        List<Dictdetail> result = null;
        //根据请求的字段获取到分页信息
        result = dictdetailService.selectByPid(pid);
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/catalog_list")
    public List catalogList(){
        return dictcatalogService.selectAll();
    }
    @ResponseBody
    @RequestMapping("/dict_save")
    public AjaxResult save(Dictdetail dictdetail){
        AjaxResult result = null;
        try {
            dictdetailService.insert(dictdetail);
            result = new AjaxResult(true,"保存成功！");
        }catch (Exception e){
            result = new AjaxResult("保存异常，请检查日志！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/dict_update")
    public AjaxResult update(Dictdetail dictdetail){
        AjaxResult result = null;
        try {
            //更新
            dictdetailService.updateByPrimaryKey(dictdetail);
            result = new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新异常，请检查日志！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/dict_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            //删除操作采用软删除，只是修改在职状态
            dictdetailService.deleteByPrimaryKey(id);
            result = new AjaxResult(true,"禁用成功！");
        }catch (Exception e){
            result = new AjaxResult("禁用失败，请检查日志！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/job_queryInDetail")
    public List<Dictdetail> job(){
        List<Dictdetail> result = dictdetailService.jobQueryInDetail();
        return result;
    }

    @ResponseBody
    @RequestMapping("/salarylevel_queryInDetail")
    public List<Dictdetail> salarylevel(){
        List<Dictdetail> result = dictdetailService.salarylevelQueryInDetail();
        return result;
    }

    @ResponseBody
    @RequestMapping("/customersource_queryInDetail")
    public List<Dictdetail> customersource(){
        List<Dictdetail> result = dictdetailService.customersourceQueryInDetail();
        return result;
    }

    @ResponseBody
    @RequestMapping("/listPlanType")
    public List<Dictdetail> listPlanType(){
        return dictdetailService.listPlanType();
    }
}


