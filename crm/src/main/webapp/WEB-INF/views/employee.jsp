<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>员工管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="emp_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="emp_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.EmployeeController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.EmployeeController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.EmployeeController:delete')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">离职</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
    <div>
        关键字查询：<input type="text" name="keyWord">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">搜索</a>
        <a class="easyui-linkbutton" href="emp_download">导出Excel</a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="emp_dialog">
    <form id="emp_form" method="post">
        <%--  隐含域，判断新增还是更新 --%>
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>账号</td>
                <td><input type="text" name="username"></td>
            </tr>
            <tr>
                <td>真实姓名</td>
                <td><input type="text" name="realname"></td>
            </tr>
            <tr>
                <td>联系方式</td>
                <td><input type="text" name="tel"></td>
            </tr>
            <tr>
                <td>邮箱</td>
                <td><input type="text" name="email"></td>
            </tr>
            <tr>
                <td>部门</td>
                <td><input type="text" class="easyui-combobox" name="dept.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'dept_queryForEmp'
                    "></td>
            </tr>
            <tr>
                <td>入职时间</td>
                <td><input type="text" name="inputTime" class="easyui-datebox"></td>
            </tr>
            <tr>
                <td>角色</td>
                <td><input id="cbRole" type="text" class="easyui-combobox" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'roles_queryForEmp',
                    multiple:true
                    "></td>
            </tr>
        </table>
    </form>
</div>
<!-- 对话框底部按钮 -->
<div id="emp_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>
<script src="js/views/employee.js"></script>
</body>
</html>
