package com.crm.web.controller;

import com.crm.domain.Attendance;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.domain.Role;
import com.crm.query.AttendanceQueryObject;
import com.crm.query.QueryObject;
import com.crm.service.IAttendanceService;
import com.crm.service.IEmployeeService;
import com.crm.util.AjaxResult;
import com.crm.util.RoleJudge;
import com.crm.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    @Autowired
    private IEmployeeService employeeService;

    @RequestMapping("/attendance")
    public String list(){
        return "attendance";
    }

    @ResponseBody
    @RequestMapping("/attendance_list")
    public PageResult queryForPage(AttendanceQueryObject queryObject,HttpSession session){
        queryObject.setUserId(((Employee) session.getAttribute(UserContext.USERINSESSION)).getId());
        queryObject.setIsHR(RoleJudge.isHR());
        return attendanceService.queryForPage(queryObject);
    }

    @ResponseBody
    @RequestMapping("/attendance_signIn")
    public AjaxResult signIn(Attendance attendance,HttpSession session){
        AjaxResult result = null;
        Employee employee = employeeService.selectByUsername(attendance.getEmp().getUsername());
        if (employee!=null){
            attendance.setEmp(employee);
        }else {
            return new AjaxResult("用户不存在！");
        }
        if (queryForSignInfo(session)!=null){
            return new AjaxResult("已签到，请不要重复签到！");
        }
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(attendance.getSignintime());
            c.set(Calendar.HOUR_OF_DAY,8);
            c.set(Calendar.MINUTE,30);
            c.set(Calendar.SECOND,0);

            if (attendance.getSignintime().getTime()>c.getTime().getTime()){
                attendance.setStatus((short) 1);
            }
            attendanceService.insert(attendance);
            result = new AjaxResult(true, "签到成功！");
        } catch (Exception e) {
            result = new AjaxResult("签到失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/attendance_signOut")
    public AjaxResult signOut(Attendance attendance,HttpSession session){
        AjaxResult result = null;
        Attendance at = queryForSignInfo(session);
        attendance.setEmp(at.getEmp());
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(attendance.getSignintime());
            c.set(Calendar.HOUR_OF_DAY,17);
            c.set(Calendar.MINUTE,30);
            c.set(Calendar.SECOND,0);

            if (attendance.getSignouttime().getTime()<c.getTime().getTime()){
                attendance.setStatus((short) 2);
            }
            attendanceService.updateByPrimaryKey(attendance);
            result = new AjaxResult(true, "签退成功！");
        } catch (Exception e) {
            result = new AjaxResult("签退失败，请联系管理员！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/attendance_resign")
    public AjaxResult resign(String type,Attendance attendance,HttpSession session){
        AjaxResult result = null;
        Employee emp = employeeService.selectByPrimaryKey(attendance.getEmp().getId());
        if (emp == null){
            return new AjaxResult("员工不存在，请重新核对！");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(attendance.getSignintime());
        c.set(Calendar.HOUR_OF_DAY,8);
        c.set(Calendar.MINUTE,30);
        c.set(Calendar.SECOND,0);
        if (attendance.getSignintime().getTime()>c.getTime().getTime()){
            attendance.setStatus((short) 1);
        }

        c.set(Calendar.HOUR_OF_DAY,17);
        c.set(Calendar.MINUTE,30);

        if (attendance.getSignouttime().getTime()<c.getTime().getTime()){
            attendance.setStatus((short) 2);
        }

        attendance.setResignemp((Employee) session.getAttribute(UserContext.USERINSESSION));
        attendance.setResigntime(new Date());

        try {
            if ("insert".equals(type)){
                attendanceService.insert(attendance);
            }
            if ("update".equals(type)){
                attendanceService.updateByPrimaryKey(attendance);
            }
            result = new AjaxResult(true,"补签成功！");
        }catch (Exception e){
            result = new AjaxResult("补签失败！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/queryForSignInfo")
    public Attendance queryForSignInfo(HttpSession session){
        Attendance attendance = null;
        Long empId = ((Employee)session.getAttribute(UserContext.USERINSESSION)).getId();
        attendance = attendanceService.queryForSignInfo(empId);
        return attendance;
    }
}
