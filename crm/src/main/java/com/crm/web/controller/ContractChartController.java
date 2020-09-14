package com.crm.web.controller;

import com.crm.domain.ContractChart;
import com.crm.domain.Employee;
import com.crm.domain.PageResult;
import com.crm.query.ContractChartQueryObject;
import com.crm.service.IContractChartService;
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
public class ContractChartController {

    @Autowired
    private IContractChartService contractChartService;

    @RequestMapping("/contract_chart")
    public String list(String type, Model model){
        model.addAttribute("type",type);
        return "contractchart";
    }

    @ResponseBody
    @RequestMapping("/contract_chart_list")
    public PageResult queryForPage(ContractChartQueryObject queryObject){
        if (queryObject.getGroupInfo()==null||queryObject.getGroupInfo().length()==0){
            queryObject.setGroupInfo("year");
        }
        return contractChartService.queryForPage(queryObject);
    }

    @RequestMapping("/display_contract_chart")
    public String display(String groupInfo,Model model){
        ContractChartQueryObject queryObject= new ContractChartQueryObject();
        queryObject.setGroupInfo(groupInfo);

        List<ContractChart> rows = contractChartService.queryForPage(queryObject).getRows();
        if (rows == null || rows.size()==0)
            return "norecord";
        //获取分组信息和值
        String[] xLabels = new String[rows.size()];
        Float[] values = new Float[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            xLabels[i] = rows.get(i).getGroupInfo();
            values[i] = rows.get(i).getMoney();
        }

        GsonOption option = ChartUtils.getLineJsonOption("销售金额报表","销售额",values,xLabels);
        model.addAttribute("option",option);

        return "lineChart";
    }
}
