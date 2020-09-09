package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    private Long id;

    private String sn;

    private Customer customer;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd" )
    private Date signtime;

    private Employee seller;

    private Float summoney;

    private Float money;

    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:ss:mm" )
    private Date paytime;

    private String intro;

    private Short status;

    private String file;

    private Employee modifyuser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:ss:mm" )
    private Date modifytime;
}