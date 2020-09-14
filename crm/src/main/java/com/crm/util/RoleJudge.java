package com.crm.util;

import com.crm.domain.Role;

import java.util.List;

public class RoleJudge {
    public static Boolean isHR(){
        List<Role> roles = (List<Role>) UserContext.get().getSession().getAttribute(UserContext.ROLEINSESSION);
        for (Role role:roles) {
            if ("HR".equals(role.getSn())){
                return true;
            }
        }
        return false;
    }

    public static String SEorBoss(){
        List<Role> roles = (List<Role>) UserContext.get().getSession().getAttribute(UserContext.ROLEINSESSION);
        for (Role role:roles) {
            if ("SE".equals(role.getSn())){
                return "SE";
            }
            if ("BOSS".equals(role.getSn())){
                return "BOSS";
            }
        }
        return "";
    }
}
