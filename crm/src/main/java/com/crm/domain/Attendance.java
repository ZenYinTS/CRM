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
public class Attendance {
    private Long id;

    private Employee emp;

    private String ip;

    private Short status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:ss:mm" )
    private Date signintime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:ss:mm" )
    private Date signouttime;

    private Employee resignemp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:ss:mm" )
    private Date resigntime;
}