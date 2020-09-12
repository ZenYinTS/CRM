package com.crm.web.controller;

import com.crm.domain.Attendance;
import com.crm.domain.Employee;
import com.crm.domain.Monthattend;
import com.crm.domain.PageResult;
import com.crm.query.AttendanceQueryObject;
import com.crm.query.MonthAttendQueryObject;
import com.crm.service.IAttendanceService;
import com.crm.service.IEmployeeService;
import com.crm.service.IMonthAttendService;
import com.crm.util.AjaxResult;
import com.crm.util.MailUtil;
import com.crm.util.UploadUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MonthAttendController {
    @Autowired
    private IAttendanceService attendanceService;

    @Autowired
    private IMonthAttendService monthAttendService;

    @Autowired
    private IEmployeeService employeeService;

    @RequestMapping("/monthAttend")
    public String list() throws ParseException {
        updateAttendance();
        return "monthAttend";
    }

    //考勤表的更新
    private void updateAttendance() throws ParseException {
        //1. 获取考勤表的所有员工
        Long[] eids = attendanceService.queryForEmpId();
        //2. 循环，对考勤表的每个员工获取其打卡的年份与月份
        for (Long eid:eids) {
            List<Attendance> attendanceList = attendanceService.queryByEid(eid);
            //年份-月份的字符串存放在set中
            Set<String> dateString = new HashSet<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (Attendance attendance : attendanceList) {
                String date = sdf.format(attendance.getSignintime());
                dateString.add(date);
            }

            //遍历set，获取年份以及对应的月份
            for (String s : dateString) {
                String[] ym = s.split("-");
                //年份
                Integer year = Integer.parseInt(ym[0]);
                //月份
                Integer month = Integer.parseInt(ym[1]);
                //在统计表中查找
                MonthAttendQueryObject queryObject = new MonthAttendQueryObject();
                queryObject.setYear(year);
                queryObject.setMonth(month);
                queryObject.setEid(eid);
                List<Monthattend> m = monthAttendService.queryForPage(queryObject).getRows();

                Employee e = employeeService.selectByPrimaryKey(eid);
                Integer monthAttendCount = attendanceService.countMonthAttend(eid, year, month, null);
                Integer monthLateCount = attendanceService.countMonthAttend(eid, year, month, Short.parseShort("1"));
                Integer monthLeaveEarlyCount = attendanceService.countMonthAttend(eid, year, month, Short.parseShort("2"));
                Monthattend monthattend = new Monthattend(null, e, sdf.parse(s), monthAttendCount, monthLateCount, monthLeaveEarlyCount);
                //不存在则添加
                if (m == null || m.size() == 0) {
                    monthAttendService.insert(monthattend);
                } else {
                    //存在则更新
                    monthAttendService.updateByPrimaryKey(monthattend);
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping("/monthAttend_list")
    public PageResult queryForPage(MonthAttendQueryObject queryObject){
        return monthAttendService.queryForPage(queryObject);
    }

    @ResponseBody
    @RequestMapping("/monthAttend_save")
    public AjaxResult save(Monthattend monthattend){
        AjaxResult result = null;
        if (monthattend.getEmp()==null||monthattend.getEmp().getId() == null)
            return new AjaxResult("用户ID不可为空！");
        Employee employee = employeeService.selectByPrimaryKey(monthattend.getEmp().getId());
        if (employee!=null){
            monthattend.setEmp(employee);
        }else {
            return new AjaxResult("用户不存在！");
        }
        try {
            monthAttendService.insert(monthattend);
            result = new AjaxResult(true, "添加成功！");
        } catch (Exception e) {
            result = new AjaxResult("添加失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/monthAttend_update")
    public AjaxResult update(Monthattend monthattend){
        AjaxResult result = null;
        if (monthattend.getEmp()==null||monthattend.getEmp().getId() == null)
            return new AjaxResult("用户ID不可为空！");
        Employee employee = employeeService.selectByPrimaryKey(monthattend.getEmp().getId());
        if (employee!=null){
            monthattend.setEmp(employee);
        }else {
            return new AjaxResult("用户不存在！");
        }
        try {
            monthAttendService.updateByPrimaryKey(monthattend);
            result = new AjaxResult(true, "更新成功！");
        } catch (Exception e) {
            result = new AjaxResult("更新失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/monthAttend_remove")
    public AjaxResult remove(Monthattend monthattend){
        AjaxResult result = null;
        try {
            monthAttendService.deleteByPrimaryKey(monthattend.getId());
            result = new AjaxResult(true, "删除成功！");
        } catch (Exception e) {
            result = new AjaxResult("删除失败，请联系管理员！");
        }
        return result;
    }

    @RequestMapping("/monthAttend_output")
    public ResponseEntity<byte[]> output(Integer year,Integer month) throws Exception {
        //1. 得到要下载的文件的流
        //找到要下载的文件的真实路径
        String downloadFilePath=UserContext.get().getSession().getServletContext().getRealPath("upload");//从我们的上传文件夹中去取
        String filename = "monthAttend.xlsx";
        //1.1 获取数据
        MonthAttendQueryObject queryObject = new MonthAttendQueryObject();
        queryObject.setYear(year);
        queryObject.setMonth(month);
        List<Monthattend> monthattendList = monthAttendService.queryForPage(queryObject).getRows();
        //1.2 创建Excel对象
        Workbook workbook = null;
        FileOutputStream fos = new FileOutputStream(downloadFilePath+ File.separator+filename);
        if (filename.endsWith(".xlsx")){
            workbook = new XSSFWorkbook();
        }
        if (filename.endsWith(".xls")){
            workbook = new HSSFWorkbook();
        }
        //1.3 创建工作本sheet
        Sheet sheet = workbook.createSheet(queryObject.getYear()+"年"+queryObject.getMonth()+"月考勤列表");
        //1.4 创建字段
        int rCount = 0;
        Row row1 = sheet.createRow(rCount);
        row1.createCell(0).setCellValue("用户ID");
        row1.createCell(1).setCellValue("日期");
        row1.createCell(2).setCellValue("该月出勤天数");
        row1.createCell(3).setCellValue("该月迟到天数");
        row1.createCell(4).setCellValue("该月早退天数");

        //1.5 填入员工信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Monthattend m:monthattendList) {
            Row r = sheet.createRow(++rCount);
            r.createCell(0).setCellValue(m.getEmp()==null?"": m.getEmp().getId().toString());
            r.createCell(1).setCellValue(m.getOutputtime()==null?"":sdf.format(m.getOutputtime()));
            r.createCell(2).setCellValue(m.getMonthAttendCount()==null?"":m.getMonthAttendCount().toString());
            r.createCell(3).setCellValue(m.getMonthLateCount()==null?"":m.getMonthLateCount().toString());
            r.createCell(4).setCellValue(m.getMonthLeaveEarlyCount()==null?"":m.getMonthLeaveEarlyCount().toString());
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
        headers.set("Content-Disposition","attachment;filename="+queryObject.getYear()+"-"+queryObject.getMonth()+filename);

        return new ResponseEntity<byte[]>(tmp,headers, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/monthAttend_send")
    public AjaxResult send(MultipartFile file, HttpServletRequest request){
        AjaxResult result = null;
        //上传到服务器
        String filePath = UploadUtils.mulipartFileUpload(file, request);
        //获取真实地址
        filePath = request.getSession().getServletContext().getRealPath(filePath);
        try {
            String[] emails = employeeService.getFEEmails();
            MailUtil.sendEmail(emails, filePath, "考勤", "考勤汇总", "text/html;charset=utf-8");
            result = new AjaxResult(true,"发送成功！");
        }catch (Exception e){
            result = new AjaxResult("发送失败，请联系管理员！");
        }
        return result;
    }
}
