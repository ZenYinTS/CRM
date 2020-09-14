package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@JsonIgnoreProperties(value = "handler")
@Data
public class Menu {
    private Long id;

    private String text;

    private String state;

    private Boolean checked;

    private String attributes;

    private List<Menu> children;

    private String iconCls;

    private String function;
}