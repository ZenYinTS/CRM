<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html >

<head>
    <meta charset="UTF-8">
    <title>客户关系管理系统-登录</title>
    <%@include file="WEB-INF/views/common.jsp"%>
</head>
<body>
<div>
    <h1 align="center">用户登录</h1>
    <form method="post" action="${ctp}/login" style="text-align: center">
        <input type="text" name="username" placeholder="请输入用户名"><br/><br/>
        <input type="password" name="password" placeholder="请输入密码"><br/><br/>
        <input type="button" value='登录' onclick="submitForm()" >
        <input type="reset" value='重置'>
    </form>
    <p align="center">Copyright ©2020- xxxx有限公司</p>
</div>
<script>
    <%--  使用键盘enter进行登录  --%>
    $(document).keyup(function(event){
        if(event.keyCode==13){
            submitForm();
        }
    });

    //异步提交表单
    function submitForm() {
        $.ajax({
            url:"${ctp}/login",
            data:$("form").serialize(),
            type:"post",
            success:function (data) {
                if (data.success){
                    window.location.href="index";
                }else {
                    alert(data.msg);
                }
            },
            dataType:"json"
        });
    }
</script>
</body>
</html>