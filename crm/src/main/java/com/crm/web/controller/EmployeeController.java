package com.crm.web.controller;

import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.EmployeeQueryObject;
import com.crm.service.IEmployeeService;
import com.crm.util.AjaxResult;
import com.crm.util.UserContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;


@Controller
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;


    @RequestMapping(value="/employee")
    public String list(){
        return "employee";
    }

    @ResponseBody
    @RequestMapping("/emp_queryForDept")
    public List<Employee> queryForDept(Long id){
        return employeeService.queryForDept(id);
    }

    @ResponseBody
    @RequestMapping(value="/employee_list")
    public PageResult employeeList(EmployeeQueryObject queryObject){
        PageResult result = null;
        //根据请求的字段获取到分页信息
        result = employeeService.queryForPage(queryObject);
        return result;
    }

    @ResponseBody
    @RequestMapping("/employee_save")
    public AjaxResult save(Employee employee){
        AjaxResult result = null;
        try {
            //employee不在表单中的字段赋值
            employee.setPassword("888888");
            employee.setAdmin(false);
            employee.setState(true);
            //插入
            employeeService.insert(employee);
            result = new AjaxResult(true,"保存成功！");
        }catch (Exception e){
            result = new AjaxResult("保存异常，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/employee_update")
    public AjaxResult update(Employee employee){
        AjaxResult result = null;
        try {
            //更新
            employeeService.updateByPrimaryKey(employee);
            result = new AjaxResult(true,"更新成功！");
        }catch (Exception e){
            result = new AjaxResult("更新异常，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("employee_remove")
    public AjaxResult remove(Long id){
        AjaxResult result = null;
        try {
            //删除操作采用软删除，只是修改在职状态
            employeeService.updateState(id);
            result = new AjaxResult(true,"离职成功！");
        }catch (Exception e){
            result = new AjaxResult("离职失败，请联系管理员！");
        }
        return result;
    }
    
    @RequestMapping("/emp_download")
    public ResponseEntity<byte[]> download() throws Exception {
        //1. 得到要下载的文件的流
        //找到要下载的文件的真实路径
        String downloadFilePath=UserContext.get().getSession().getServletContext().getRealPath("upload");//从我们的上传文件夹中去取
        String filename = "poi_out.xlsx";
        //1.1 获取员工
        List<Employee> employees = employeeService.selectAll();
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
        Sheet sheet = workbook.createSheet("员工列表");
        //1.4 创建字段
        int rCount = 0;
        Row row1 = sheet.createRow(rCount);
        row1.createCell(0).setCellValue("员工账号");
        row1.createCell(1).setCellValue("真实姓名");
        row1.createCell(2).setCellValue("电话");
        row1.createCell(3).setCellValue("邮箱");
        row1.createCell(4).setCellValue("部门");
        row1.createCell(5).setCellValue("入职时间");
        row1.createCell(6).setCellValue("状态");
        row1.createCell(7).setCellValue("是否管理员");
        
        //1.5 填入员工信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Employee e:employees) {
            Row r = sheet.createRow(++rCount);
            r.createCell(0).setCellValue(e.getUsername()==null?"":e.getUsername());
            r.createCell(1).setCellValue(e.getRealname()==null?"":e.getRealname());
            r.createCell(2).setCellValue(e.getTel()==null?"":e.getTel());
            r.createCell(3).setCellValue(e.getEmail()==null?"":e.getEmail());
            r.createCell(4).setCellValue(e.getDept()==null?"":e.getDept().getName());
            r.createCell(5).setCellValue(e.getInputTime()==null?"":sdf.format(e.getInputTime()));
            r.createCell(6).setCellValue(e.getState()?"正常":"离职");
            r.createCell(7).setCellValue(e.getAdmin()?"是":"否");
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

        return new ResponseEntity<byte[]>(tmp,headers,HttpStatus.OK);
     }
}


