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
public class Customer {
    private Long id;

    private String name;

    private Integer age;

    private Integer gender;

    private String tel;

    private String email;

    private String qq;

    private String wechat;

    private Dictdetail job;

    private Dictdetail salarylevel;

    private Dictdetail customersource;

    private Employee inchargeuser;

    private Employee inputuser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss" )
    private Date inputtime;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss" )
    private Date formaltime;
}