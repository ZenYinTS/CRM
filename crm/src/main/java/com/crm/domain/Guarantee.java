package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guarantee {
    private Long id;

    private String sn;

    private String productname;

    private Customer customer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:ss:mm" )
    private Date duetime;

    private String remark;
}