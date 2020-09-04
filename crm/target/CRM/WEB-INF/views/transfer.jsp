<%@ page import="com.crm.domain.Employee" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>客户移交记录管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="transfer_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="transfer_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.TransferController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">创建客户移交</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
    <div>
        客户姓名：<input type="text" name="cName">
        开始时间：<input id="startTime" type="text" class="easyui-datebox" name="startTime">
         - <input id="endTime" type="text" class="easyui-datebox" name="endTime">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">查询</a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="transfer_dialog">
    <form id="transfer_form" method="post">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>客户选择：</td>
                <td><input type="text" class="easyui-combobox" name="customer.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'customer_all',
                    panelHeight:'auto'
                    "></td>
            </tr>
            <tr>
                <td>新市场专员：</td>
                <td><input type="text" class="easyui-combobox" name="newseller.id" data-options="
                    valueField: 'id',
                    textField: 'username',
                    url:'sell_all?inchargeId=-1',
                    panelHeight:'auto'
                    "></td>
            </tr>
            <tr>
                <td>移交原因：</td>
                <td><input type="text" name="transreason"></td>
            </tr>
        </table>
    </form>
</div>
<!-- 对话框底部按钮 -->
<div id="transfer_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>
<script src="js/views/transfer.js"></script>
</body>
</html>
