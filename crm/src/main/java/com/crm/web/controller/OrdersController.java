package com.crm.web.controller;

import com.crm.domain.Contract;
import com.crm.domain.Employee;
import com.crm.domain.Orders;
import com.crm.domain.PageResult;
import com.crm.query.OrdersQueryObject;
import com.crm.service.IContractService;
import com.crm.service.ICustomerService;
import com.crm.service.IOrdersService;
import com.crm.util.AjaxResult;
import com.crm.util.UploadUtils;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Controller
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IContractService contractService;

    @Autowired
    private ICustomerService customerService;

    @RequestMapping("/orders")
    public String list() {
        return "orders";
    }

    @ResponseBody
    @RequestMapping("/orders_list")
    public PageResult queryForPage(OrdersQueryObject queryObject) {
        return ordersService.queryForPage(queryObject);
    }


    @ResponseBody
    @RequestMapping("/orders_save")
    public AjaxResult save(Orders orders, @RequestParam(value = "pic", required = false) MultipartFile pic, HttpServletRequest request) {
        String filePath = UploadUtils.mulipartFileUpload(pic, request);
        AjaxResult result = null;
        //不在对话框中的字段赋值
        orders.setStatus((short) 0);
        orders.setCreatetime(new Date());
        orders.setFile(filePath);
        try {
            ordersService.insert(orders);
            result = new AjaxResult(true, "添加成功！");
        } catch (Exception e) {
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/orders_update")
    public AjaxResult update(Orders orders, @RequestParam(value = "pic", required = false) MultipartFile pic, HttpSession session, HttpServletRequest request) {
        String filePath = UploadUtils.mulipartFileUpload(pic, request);
        Employee emp = (Employee) session.getAttribute(UserContext.USERINSESSION);
        AjaxResult result = null;
        //不在对话框中的字段赋值
        orders.setFile(filePath);
        orders.setModifyuser(emp);
        orders.setModifytime(new Date());
        try {
            ordersService.updateByPrimaryKey(orders);
            result = new AjaxResult(true, "更新成功！");
        } catch (Exception e) {
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/orders_remove")
    public AjaxResult remove(Long id) {
        AjaxResult result = null;
        try {
            ordersService.deleteByPrimaryKey(id);
            result = new AjaxResult(true, "删除成功！");
        } catch (Exception e) {
            result = new AjaxResult("删除失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/orders_check")
    public AjaxResult check(Long id, String option) {
        AjaxResult result = null;
        try {
            Orders orders = ordersService.selectByPrimaryKey(id);
            if ("permit".equals(option))
                orders.setStatus((short) (orders.getStatus() + 1));
            if ("reject".equals(option))
                orders.setStatus((short) 4);

            ordersService.updateByPrimaryKey(orders);
            if (orders.getStatus() == 3){
                Contract contract = new Contract(null, UUID.randomUUID().toString(),
                        orders.getCustomer(),orders.getSigntime(),orders.getSeller(),orders.getTotalsum(),
                        null,null,orders.getIntro(),(short)0,orders.getFile(),orders.getModifyuser(),orders.getModifytime());
                contractService.insert(contract);
            }
            result = new AjaxResult(true, "审核完成！");
        } catch (Exception e) {
            System.out.println(e);
            result = new AjaxResult("审核失败，请联系管理员！");
        }
        return result;
    }

    @RequestMapping("/orders_download")

    public ResponseEntity<byte[]> download(String path) throws Exception {
        return UploadUtils.download(path);
    }
}
