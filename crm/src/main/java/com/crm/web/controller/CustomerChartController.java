package com.crm.web.controller;

import com.crm.domain.CustomerChart;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.domain.Role;
import com.crm.query.CustomerChartQueryObject;
import com.crm.service.ICustomerChartService;
import com.crm.service.IEmployeeService;
import com.crm.util.ChartUtils;
import com.crm.util.UserContext;
import com.github.abel533.echarts.json.GsonOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.*;

@Controller
public class CustomerChartController {

    @Autowired
    private ICustomerChartService customerChartService;

    @Autowired
    private IEmployeeService employeeService;

    private PageResult queryResult;

    @RequestMapping("/customer_chart")
    public String list(String type, Model model){
        model.addAttribute("type",type);
        return "customerchart";
    }

    @ResponseBody
    @RequestMapping("/chart_list")
    public PageResult queryForPage(CustomerChartQueryObject queryObject, HttpSession session){
        if (queryObject.getGroupInfo()==null||queryObject.getGroupInfo().length()==0){
            queryObject.setGroupInfo("year");
        }
        //设置身份
        queryObject.setSn(SEorBoss());

        queryObject.setUserId(((Employee) session.getAttribute(UserContext.USERINSESSION)).getId());
        queryResult =  customerChartService.queryForPage(queryObject);
        return queryResult;
    }

    private String SEorBoss(){
        List<Role> roles = (List<Role>) UserContext.get().getSession().getAttribute(UserContext.ROLEINSESSION);
        for (Role role:roles) {
            if ("SE".equals(role.getSn())){
                return "SE";
            }
            if ("BOSS".equals(role.getSn())){
                return "BOSS";
            }
        }
        return "";
    }

    @RequestMapping(value = "/display_chart")
    public String display(String type,Model model){
        List<CustomerChart> rows = queryResult.getRows();
        if (rows == null || rows.size()==0)
            return "norecord";

        Map<String,Object> params = getParam(rows);
        //为values赋值
        Long[][] values = (Long[][]) params.get("values");

        //获取分组信息
        List<Object> objects = (List<Object>) params.get("groupInfos");
        String[] xLabels= new String[objects.size()];
        int index = 0;
        for (Object o:objects) {
            xLabels[index++] = (String)o;
        }
        String title = "";
        String[] legend = (String[]) params.get("enames");
        if ("potential".equals(type)){
            title = "潜在客户新增报表";
        }
        if ("formal".equals(type)){
            title = "正式客户新增报表";
        }

        GsonOption option = ChartUtils.getBarJsonOption(title,legend,xLabels,values);
        model.addAttribute("option",option);
        model.addAttribute("title",title);
        return "barChart";
    }

    private Map<String,Object> getParam(List<CustomerChart> rows) {
        Map<String,Object> map = new HashMap<>();
        //获取所有eid与groupInfo
        Set<Long> eidsRand = new HashSet<>();
        Set<String> groupInfosRand = new HashSet<>();
        for (CustomerChart cc:rows){
            eidsRand.add(cc.getEmp().getId());
            groupInfosRand.add(cc.getGroupInfo());
        }
        //去重
        List<Long> eids = new ArrayList<>();
        eids.addAll(eidsRand);
        List<String> groupInfos = new ArrayList<>();
        groupInfos.addAll(groupInfosRand);
        //排序
        Collections.sort(eids);
        Collections.sort(groupInfos);

        //获取员工姓名
        String[] enames = new String[eids.size()];
        int index=0;
        for (Long eid:eids){
            String s = employeeService.selectByPrimaryKey(eid).getUsername()+" - "+
                    employeeService.selectByPrimaryKey(eid).getRealname();
            enames[index++]= s;
        }

        //存入map
        map.put("eids",eids);
        map.put("enames",enames);
        map.put("groupInfos", groupInfos);

        Long[][] values = new Long[eids.size()][groupInfos.size()];
        //遍历赋值
        for (int i = 0; i < eids.size(); i++) {
            Long eid = eids.get(i);
            for (int j = 0; j < groupInfos.size(); j++) {
                String info = groupInfos.get(j);
                values[i][j] = getCustomerCount(rows,eid,info);
            }
        }

        //存入map
        map.put("values",values);

        return map;
    }

    private Long getCustomerCount(List<CustomerChart> rows,Long eid,String groupInfo){
        for (CustomerChart cc:rows){
            if (cc.getEmp().getId()==eid&&groupInfo.equals(cc.getGroupInfo())){
                return cc.getCustomerCount();
            }
        }
        return 0l;
    }
}
