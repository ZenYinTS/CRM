package com.crm.query;

import lombok.Data;

@Data
public class SalaryQueryObject extends QueryObject{
    private String keyWord;
    private Integer year;
    private Integer month;

    private Long userId;
    private Boolean isHR;
}
