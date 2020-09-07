package com.crm.web.controller;

import com.crm.domain.*;
import com.crm.query.CustomerQueryObject;
import com.crm.service.ICustomerService;
import com.crm.service.ITransferService;
import com.crm.util.AjaxResult;
import com.crm.util.UserContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITransferService transferService;

    @RequestMapping("/customer")
    public String list(String type,Model model){
        model.addAttribute("type",type);
        return "customer";
    }

    @ResponseBody
    @RequestMapping("/customer_all")
    public List<Customer> all(){
        return customerService.selectAll();
    }

    @ResponseBody
    @RequestMapping("/customer_list")
    public PageResult queryForPage(String type,CustomerQueryObject queryObject){
        Employee e = (Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION);
        if (queryObject.getUserId() == null) {
            queryObject.setUserId(e.getId());
        }
        if (queryObject.getStatus()  == null){
            if ("potential".equals(type))
                queryObject.setStatus(0);
            if ("formal".equals(type))
                queryObject.setStatus(1);
            if ("resource".equals(type))
                queryObject.setStatus(2);
        }
        if ("resource".equals(type))
            queryObject.setIsSE(true);
        else
            queryObject.setIsSE(isSE(e));

        return customerService.queryForPage(queryObject);
    }

    private Boolean isSE(Employee e){
        List<Role> userRoles = (List<Role>) UserContext.get().getSession().getAttribute(UserContext.ROLEINSESSION);
        for (Role r : userRoles) {
            if ("SE".equals(r.getSn()))
                return true;
        }
        return false;
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
    @RequestMapping("/customer_takein")
    public AjaxResult takein(Long id){
        AjaxResult result  = null;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(1);
        try{
            customerService.updateByPrimaryKey(customer);
            result = new AjaxResult(true,"吸纳资源池客户成功！");
        }catch (Exception e){
            result = new AjaxResult("吸纳客户失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/customer_move")
    public AjaxResult move(Long id){
        AjaxResult result  = null;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(2);
        try{
            customerService.updateByPrimaryKey(customer);
            result = new AjaxResult(true,"成功移入资源池！");
        }catch (Exception e){
            result = new AjaxResult("移入资源池失败，请联系管理员！");
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
    @RequestMapping("/customer_lose")
    public AjaxResult lose(Long id){
        AjaxResult result = null;
        try {
            customerService.updateStatus(id,-2);
            result = new AjaxResult(true,"操作成功！");
        }catch (Exception e){
            result = new AjaxResult("操作失败，请联系管理员！");
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
    @RequestMapping("/potential_customer")
    public List<Customer> listPotentialCustomer(){
        CustomerQueryObject queryObject = new CustomerQueryObject();
        Employee e = (Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION);
        queryObject.setIsSE(isSE(e));
        queryObject.setStatus(0);
        queryObject.setUserId(e.getId());
        queryObject.setPage(null);
        return customerService.queryForPageOnly(queryObject);
    }

    @ResponseBody
    @RequestMapping("/formal_customer")
    public List<Customer> listFormalCustomer(){
        CustomerQueryObject queryObject = new CustomerQueryObject();
        Employee e = (Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION);
        queryObject.setIsSE(isSE(e));
        queryObject.setStatus(1);
        queryObject.setUserId(e.getId());
        queryObject.setPage(null);
        return customerService.queryForPageOnly(queryObject);
    }

    @RequestMapping("/customer_output")
    public ResponseEntity<byte[]> output() throws Exception {
        //1. 得到要下载的文件的流
        //找到要下载的文件的真实路径
        String downloadFilePath=UserContext.get().getSession().getServletContext().getRealPath("upload");//从我们的上传文件夹中去取
        String filename = "customer.xlsx";
        //1.1 获取员工
        List<Customer> customers = customerService.selectAllFormal();
        //1.2 创建Excel对象
        Workbook workbook = null;
        FileOutputStream fos = new FileOutputStream(downloadFilePath+File.separator+filename);
        if (filename.endsWith(".xlsx")){
            workbook = new XSSFWorkbook();
        }
        if (filename.endsWith(".xls")){
            workbook = new HSSFWorkbook();
        }
        //1.3 创建工作本sheet
        Sheet sheet = workbook.createSheet("客户列表");
        //1.4 创建字段
        int rCount = 0;
        Row row1 = sheet.createRow(rCount);
        row1.createCell(0).setCellValue("编号");
        row1.createCell(1).setCellValue("客户姓名");
        row1.createCell(2).setCellValue("年龄");
        row1.createCell(3).setCellValue("性别");
        row1.createCell(4).setCellValue("电话号码");
        row1.createCell(5).setCellValue("邮箱");
        row1.createCell(6).setCellValue("QQ");
        row1.createCell(7).setCellValue("微信");
        row1.createCell(8).setCellValue("职业");
        row1.createCell(9).setCellValue("收入水平");
        row1.createCell(10).setCellValue("客户来源");
        row1.createCell(11).setCellValue("负责人");
        row1.createCell(12).setCellValue("创建人");
        row1.createCell(13).setCellValue("日期");
        row1.createCell(14).setCellValue("状态");

        //1.5 填入员工信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Customer c:customers) {
            Row r = sheet.createRow(++rCount);
            r.createCell(0).setCellValue(c.getId()==null?"": c.getId().toString());
            r.createCell(1).setCellValue(c.getName()==null?"":c.getName());
            r.createCell(2).setCellValue(c.getAge()==null?"":c.getAge().toString());
            r.createCell(3).setCellValue(c.getGender()==1?"男":"女");
            r.createCell(4).setCellValue(c.getTel()==null?"":c.getTel());
            r.createCell(5).setCellValue(c.getEmail()==null?"":c.getEmail());
            r.createCell(6).setCellValue(c.getQq()==null?"":c.getQq());
            r.createCell(7).setCellValue(c.getWechat()==null?"":c.getWechat());
            r.createCell(8).setCellValue(c.getJob()==null?"":c.getJob().getName());
            r.createCell(9).setCellValue(c.getSalarylevel()==null?"":c.getSalarylevel().getName());
            r.createCell(10).setCellValue(c.getCustomersource()==null?"":c.getCustomersource().getName());
            r.createCell(11).setCellValue(c.getInchargeuser()==null?"":c.getInchargeuser().getUsername() + " - " + c.getInchargeuser().getRealname());
            r.createCell(12).setCellValue(c.getInputuser()==null?"":c.getInputuser().getUsername() + " - " + c.getInputuser().getRealname());
            r.createCell(13).setCellValue(c.getInputtime()==null?"":sdf.format(c.getInputtime()));
            r.createCell(14).setCellValue("正式客户");
        }
        //1.6 写入表格
        workbook.write(fos);
        //1.7 关闭输出流
        fos.close();

        //获取输入流
        FileInputStream fis = new FileInputStream(downloadFilePath+File.separator+filename);//新建一个文件

        byte[] tmp = new byte[fis.available()];
        fis.read(tmp);
        fis.close();

        //2. 将下载的文件流返回
        //文件下载响应头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition","attachment;filename="+filename);

        return new ResponseEntity<byte[]>(tmp,headers, HttpStatus.OK);
    }
}
