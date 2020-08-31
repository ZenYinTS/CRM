<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>部门管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="dept_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="dept_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.DepartmentController:ALL')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">停用</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
    <div>
        关键字查询：<input type="text" name="keyWord">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">搜索</a>
        <a class="easyui-linkbutton" href="dept_download">导出Excel</a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="dept_dialog">
    <form id="dept_form" method="post">
        <%--  隐含域，判断新增还是更新 --%>
        <input id="id" type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>部门编号</td>
                <td><input type="text" name="sn"></td>
            </tr>
            <tr>
                <td>部门名称</td>
                <td><input type="text" name="name"></td>
            </tr>
            <tr>
                <td>部门经理</td>
                <td>
                    <input id="cbManager" type="text" class="easyui-combobox" name="manager.id" data-options="
                    valueField: 'id',
                    textField: 'username',
	                 url:'emp_queryForDept'
                    "></td>
            </tr>
            <tr>
                <td>上级部门</td>
                <td><input id="cbParent" type="text" class="easyui-combobox" name="parent.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'dept_queryForDept'
                    "></td>
            </tr>
        </table>
    </form>
</div>
<!-- 对话框底部按钮 -->
<div id="dept_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>
<script src="js/views/department.js"></script>
</body>
</html>
