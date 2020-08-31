package com.crm.query;

import lombok.Data;

@Data
public class PermissionQueryObject extends QueryObject{
    private Long rid;
    private String keyWord;    //查询时用到的关键字

}
