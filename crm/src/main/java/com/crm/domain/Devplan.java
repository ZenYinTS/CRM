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
public class Devplan {
    private Long id;

    private Date plantime;

    private String plansubject;

    private String plandetails;

    private Dictdetail plantype;

    private Short traceresult;

    private String remark;

    private Customer customer;

    private Employee inputuser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss" )
    private Date inputtime;

    private Boolean type;
}