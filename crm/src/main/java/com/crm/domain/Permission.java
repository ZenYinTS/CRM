package com.crm.domain;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class Permission {
    private Long id;

    @NotBlank(message = "权限名称不可为空")
    private String sn;

    @NotBlank(message = "资源地址不可为空")
    private String resource;

    //由于这里不要求从权限获取到所有角色，所以可以不用添加角色属性
}