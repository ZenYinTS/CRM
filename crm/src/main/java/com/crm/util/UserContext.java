package com.crm.util;

import javax.servlet.http.HttpServletRequest;

public class UserContext {
    public final static String USERINSESSION = "USER_IN_SESSION";
    public static final String PERMISSIONINSESSION = "PERMISSION_IN_SESSION" ;
    public static final String MENUINSESSION = "MENU_IN_SESSION";
    public static final String ROLEINSESSION = "ROLE_IN_SESSION";
    //设置ThreadLocal对象，泛型写的是所需要获取的变量类型
    private static ThreadLocal<HttpServletRequest> local = new ThreadLocal<HttpServletRequest>();
    //设置相关的set与get方法
    public static void set(HttpServletRequest request){
        local.set(request);
    }
    public static HttpServletRequest get(){
        return local.get();
    }
}
