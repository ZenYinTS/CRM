package com.crm.query;

import com.crm.domain.Employee;
import com.crm.util.UserContext;
import lombok.Data;

@Data
public class CustomerQueryObject extends QueryObject{

    private String keyWord;    //查询时用到的关键字

    private Integer status;    //查询条件，默认显示潜在客户

    private Long userId = ((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId();
}
