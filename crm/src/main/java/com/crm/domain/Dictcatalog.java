package com.crm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dictcatalog {
    private Long id;

    private String sn;

    private String name;

    private String intro;
}