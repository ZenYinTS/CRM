package com.crm.web.controller;

import com.crm.domain.Customer;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.domain.Transfer;
import com.crm.query.TransferQueryObject;
import com.crm.service.ICustomerService;
import com.crm.service.IEmployeeService;
import com.crm.service.ITransferService;
import com.crm.util.AjaxResult;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class TransferController {

    @Autowired
    private ITransferService transferService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IEmployeeService employeeService;

    @RequestMapping("/transfer")
    public String list(){
        return "transfer";
    }

    @ResponseBody
    @RequestMapping("/transfer_list")
    public PageResult queryForPage(TransferQueryObject queryObject){
        return transferService.queryForPage(queryObject);
    }

    @ResponseBody
    @RequestMapping("/transfer_save")
    public AjaxResult save(Transfer transfer){
        AjaxResult result = null;
        Customer customer = transfer.getCustomer();
        Employee newSeller = transfer.getNewseller();
        String transreason = transfer.getTransreason();

        if (customer == null || customer.getId() == null){
            return new AjaxResult("移交客户不可为空！");
        }
        if (newSeller == null || newSeller.getId() == null){
            return new AjaxResult("新市场专员不可为空！");
        }
        if (transreason == null || transreason.trim().length() == 0 ){
            return new AjaxResult("移交原因不可为空！");
        }
        //不在对话框中的字段赋值
        //旧市场专员赋值
        Customer c = customerService.selectByPrimaryKey(customer.getId());
        Employee oldSeller = employeeService.selectByPrimaryKey(c.getInchargeuser().getId());
        transfer.setOldseller(oldSeller);
        //移交时间
        transfer.setTranstime(new Date());
        //操作人
        transfer.setTransuser((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION));

        try{
            transferService.insert(transfer);
            result = new AjaxResult(true,"创建成功！");
        }catch (Exception e){
            System.out.println(e);
            result = new AjaxResult("创建失败，请联系管理员！");
        }
        return result;
    }
}
