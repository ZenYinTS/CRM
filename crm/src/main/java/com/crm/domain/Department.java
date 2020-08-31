package com.crm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private Long id;

    private String sn;

    private String name;

    private Employee manager;

    private Department parent;

    private Boolean state = true;

}