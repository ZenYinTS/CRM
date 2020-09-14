package com.crm.query;

import lombok.Data;

@Data
public class MonthAttendQueryObject extends QueryObject{
    private Long eid;
    private Integer year;
    private Integer month;

    private Long userId;
    private Boolean isHR;
}
