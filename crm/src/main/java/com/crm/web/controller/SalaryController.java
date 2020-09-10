package com.crm.web.controller;

import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.domain.Salarytable;
import com.crm.query.SalaryQueryObject;
import com.crm.service.IEmployeeService;
import com.crm.service.ISalaryService;
import com.crm.util.UploadUtils;
import com.crm.util.UserContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @Autowired
    private IEmployeeService employeeService;

    @RequestMapping("/salary")
    public String list(){
        return "salary";
    }

    @ResponseBody
    @RequestMapping("/salary_list")
    public PageResult queryForPage(SalaryQueryObject queryObject){
        return salaryService.queryForPage(queryObject);
    }

    @RequestMapping("/salary_upload")
    public String upload(MultipartFile file, HttpServletRequest request) throws Exception {
        String filePath = UploadUtils.mulipartFileUpload(file, request);
        filePath = UserContext.get().getSession().getServletContext().getRealPath(filePath);
        org.apache.poi.ss.usermodel.Workbook wb = null;
        FileInputStream fis = new FileInputStream(filePath);
        if (filePath.endsWith(".xlsx")){
            wb = new XSSFWorkbook(fis);
        }
        if (filePath.endsWith(".xls")){
            wb = new HSSFWorkbook(fis);
        }
        org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = firstRowNum+1;i < lastRowNum;i++){
            //获取当前行
            Row row = sheet.getRow(i);
            if (row==null)
                continue;
            Long id = new Double(row.getCell(0).toString()).longValue();
            Integer year = new Double(row.getCell(1).toString()).intValue();
            Integer month = new Double(row.getCell(2).toString()).intValue();
            Float salary = Float.parseFloat(row.getCell(3).toString());
            String username = row.getCell(4).toString();
            try {
                Float.parseFloat(username);
                Long longUser = new Double(username).longValue();
                username = longUser.toString();
            }catch (NumberFormatException e){}
            Employee emp = employeeService.selectByUsername(username);
            Salarytable salarytable = new Salarytable(id,year,month,salary,emp);
            salaryService.insert(salarytable);
        }
        fis.close();
        System.out.println("导入成功！");
        return "salary";
    }
}
