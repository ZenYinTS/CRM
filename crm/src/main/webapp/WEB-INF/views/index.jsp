<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta charset="UTF-8">
    <title>客户关系管理系统-首页</title>
    <%@include file="common.jsp"%>
</head>

<body>
    <div class="easyui-layout" fit="true" style="width:100%;height:100%;">
        <div data-options="region:'north'" style="height:50px;background: url('${ctp}/img/banner-pic.gif') no-repeat;background-size: cover">
            <h1>客户关系管理系统</h1>
        </div>

        <div data-options="region:'west'" class="easyui-accordion" style="width:300px;">
            <div title="菜单">
                <ul class="easyui-tree"></ul>
            </div>
            <div title="帮助">
                <ul class="easyui-tree">
                    <li>
                        <span>员工管理2</span>
                    </li>
                    <li>
                        <span>员工管理3</span>
                    </li>
                </ul>
            </div>
        </div>
        <div data-options="region:'center'">
            <div class="easyui-tabs" style="height:100%">
                <div title="欢迎页" style="padding:10px" data-options="closable:true">
                    欢迎
                </div>
            </div>
        </div>
        <div data-options="region:'south',split:true" style="height:20px;background: url('${ctp}/img/banner-pic.gif') no-repeat;background-size: cover;text-align: center">
            版权所有
        </div>
    </div>
    <script src="js/views/index.js"></script>
</body>
</html>