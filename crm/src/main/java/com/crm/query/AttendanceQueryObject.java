package com.crm.query;

import lombok.Data;

@Data
public class AttendanceQueryObject extends QueryObject{
    private Long userId;

    private Boolean isHR;
}
