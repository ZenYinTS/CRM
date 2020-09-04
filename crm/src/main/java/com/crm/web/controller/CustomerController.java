package com.crm.web.controller;

import com.crm.domain.*;
import com.crm.query.CustomerQueryObject;
import com.crm.service.ICustomerService;
import com.crm.service.IEmployeeService;
import com.crm.service.ITransferService;
import com.crm.util.AjaxResult;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ITransferService transferService;

    @RequestMapping("/customer")
    public String list(){
        return "customer";
    }

    @ResponseBody
    @RequestMapping("/customer_all")
    public List<Customer> all(){
        return customerService.selectAll();
    }

    @ResponseBody
    @RequestMapping("/customer_list")
    public PageResult queryForPage(CustomerQueryObject queryObject){
        return customerService.queryForPage(queryObject);
    }

    @ResponseBody
    @RequestMapping("/customer_save")
    public AjaxResult save(Customer customer){
        AjaxResult result = null;
        //对没有出现在对话框的字段进行赋值
        customer.setInchargeuser((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION));
        customer.setInputuser((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION));
        customer.setStatus(0);
        customer.setInputtime(new Date());
        try{
            customerService.insert(customer);
            result =  new AjaxResult(true,"添加成功！");
        }catch (Exception e){
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/customer_update")
    public AjaxResult update(Customer customer){
        AjaxResult result = null;
        customer.setInchargeuser((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION));

        try{
            customerService.updateByPrimaryKey(customer);
            result =  new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/customer_share")
    public AjaxResult share(Long id){
        AjaxResult result = null;
        try {
            customerService.updateStatus(id,2);
            result = new AjaxResult(true,"共享成功，请在资源池查看！");
        }catch (Exception e){
            result = new AjaxResult("共享失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/customer_handOver")
    public AjaxResult handover(Customer customer){
        AjaxResult result = null;
        try{
            customerService.updateByPrimaryKey(customer);
            Transfer transfer = new Transfer();
            transfer.setCustomer(customer);
            transfer.setTransreason("个人移交");
            transfer.setTranstime(new Date());
            transfer.setTransuser((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION));
            transfer.setOldseller((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION));
            transfer.setNewseller(customer.getInchargeuser());
            transferService.insert(transfer);

            result = new AjaxResult(true,"移交成功！");
        }catch (Exception e){
            result = new AjaxResult("移交失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/customer_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            customerService.updateStatus(id,-1);
            result = new AjaxResult(true,"操作成功！");
        }catch (Exception e){
            result = new AjaxResult("操作失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/customer_formal")
    public AjaxResult formal(Long id){
        AjaxResult result = null;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(1);
        customer.setFormaltime(new Date());
        try { ;
            customerService.updateByPrimaryKey(customer);
            result = new AjaxResult(true,"转正成功！");
        }catch (Exception e){
            result = new AjaxResult("转正失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/job_queryInDetail")
    public List<Dictdetail> job(){
        List<Dictdetail> result = customerService.jobQueryInDetail();
        return result;
    }

    @ResponseBody
    @RequestMapping("/salarylevel_queryInDetail")
    public List<Dictdetail> salarylevel(){
        List<Dictdetail> result = customerService.salarylevelQueryInDetail();
        return result;
    }

    @ResponseBody
    @RequestMapping("/customersource_queryInDetail")
    public List<Dictdetail> customersource(){
        List<Dictdetail> result = customerService.customersourceQueryInDetail();
        return result;
    }

}
