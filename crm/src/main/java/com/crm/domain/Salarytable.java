package com.crm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salarytable {
    private Long id;

    private Integer year;

    private Integer month;

    private Float salary;

    private Employee emp;
}