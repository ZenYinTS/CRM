package com.crm.query;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ContractQueryObject extends QueryObject{
    private String keyWord;

    private Short status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
