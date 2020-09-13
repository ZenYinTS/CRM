package com.crm.web.controller;

import com.crm.domain.CustomerChart;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.CustomerChartQueryObject;
import com.crm.service.ICustomerChartService;
import com.crm.util.ChartUtils;
import com.crm.util.UserContext;
import com.github.abel533.echarts.json.GsonOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CustomerChartController {

    @Autowired
    private ICustomerChartService customerChartService;

    @RequestMapping("/customer_chart")
    public String list(String type, Model model){
        model.addAttribute("type",type);
        return "customerchart";
    }

    @ResponseBody
    @RequestMapping("/chart_list")
    public PageResult queryForPage(CustomerChartQueryObject queryObject){
        if (queryObject.getGroupInfo()==null||queryObject.getGroupInfo().length()==0){
            queryObject.setGroupInfo("year");
        }
        queryObject.setUserId(((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId());
        System.out.println(queryObject);
        return customerChartService.queryForPage(queryObject);
    }

    @RequestMapping("/display_chart")
    public String display(String groupInfo,String type,Model model){
        CustomerChartQueryObject queryObject= new CustomerChartQueryObject();
        queryObject.setType(type);
        queryObject.setGroupInfo(groupInfo);
        queryObject.setUserId(((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId());

        List<CustomerChart> rows = customerChartService.queryForPage(queryObject).getRows();
        //获取分组信息和值
        String[] xLabels = new String[rows.size()];
        Long[] values = new Long[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            xLabels[i] = rows.get(i).getGroupInfo();
            values[i] = rows.get(i).getCustomerCount();
        }
        String title = "",legend = "";
        if ("potential".equals(type)){
            title = "潜在客户新增报表";
            legend = "潜在客户新增数量";
        }
        if ("formal".equals(type)){
            title = "正式客户新增报表";
            legend = "正式客户新增数量";
        }

        GsonOption option = ChartUtils.getBarJsonOption(title,legend,xLabels,values);
        model.addAttribute("option",option);
        model.addAttribute("title",title);

        return "barChart";
    }
}
