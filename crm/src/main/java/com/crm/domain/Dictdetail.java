package com.crm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dictdetail {
    private Long id;

    private Dictcatalog parent;

    private String name;

    private String sn;

    private String intro;
}