package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.Date;

@Data
public class Orders {
    private Long id;

    private Customer customer;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd" )
    private Date signtime;

    private Employee seller;

    private Float totalsum;

    private Float summoney;

    private String intro;

    private String file;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss" )
    private Date createtime;

    private Employee modifyuser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss" )
    private Date modifytime;

    private Short status;
}