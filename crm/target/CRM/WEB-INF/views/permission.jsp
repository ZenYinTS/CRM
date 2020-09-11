<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>权限管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="permission_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="permission_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.PermissionController:ALL')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
    <div>
        关键字查询：<input type="text" name="keyWord">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">搜索</a>
        <a class="easyui-linkbutton" href="permission_download">导出Excel</a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="permission_dialog">
    <form id="permission_form" method="post">
        <%--  隐含域，判断新增还是更新 --%>
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>权限名称</td>
                <td><input type="text" name="sn"/></td>
            </tr>
            <tr>
                <td>资源地址</td>
                <td><input type="text" name="resource"/> </td>
            </tr>
        </table>
    </form>
</div>
<!-- 对话框底部按钮 -->
<div id="permission_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>
<script src="js/views/permission.js"></script>
</body>
</html>
