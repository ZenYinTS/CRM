<%@ page import="com.crm.domain.Employee" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>订单管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="orders_datagrid"></table>
<input type="hidden" id="type" name="type" value="${type}">
<!-- 工具栏按钮 -->
<div id="orders_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.OrdersController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.OrdersController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.OrdersController:remove')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.OrdersController:check')}">
            <a id="btnCheck" iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="check">审核</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.OrdersController:dcheck')}">
            <a id="btnDCheck" iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="check">部门审核</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.OrdersController:fcheck')}">
            <a id="btnFCheck" iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="check">财务审核</a>
        </c:if>
    </div>
    <div>
        关键字：<input type="text" name="keyWord">
        <select id="orderStatus" class="easyui-combobox" name="status" style="width:100px;"
                data-options="panelHeight:'auto'">
            <option value="">全部</option>
            <option value="0">初始录入</option>
            <option value="1">部门未审核</option>
            <option value="2">财务未审核</option>
            <option value="3">已生成合同</option>
            <option value="4">已退款</option>
        </select>
        开始日期：<input id="startTime" type="text" class="easyui-datebox" name="startTime">
        结束日期：<input id="endTime" type="text" class="easyui-datebox" name="endTime">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn"></a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="orders_dialog">
    <form id="orders_form" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>定金客户：</td>
                <td><input id="customerId" type="text" class="easyui-combobox" name="customer.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'customer_all',
                    panelHeight:'auto'
                    "></td>
            </tr>
            <tr>
                <td>签订时间：</td>
                <td><input id="signtime" type="text" class="easyui-datebox" name="signtime"></td>
            </tr>
            <tr>
                <td>销售人员姓名：</td>
                <td>
                    <input type="hidden" class="easyui-combobox" name="seller.id"
                           value="<%= ((Employee)UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId()%>">
                    <input type="text" class="easyui-combobox" name="seller.username"
                           value="<%= ((Employee)UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getUsername()%>"
                           disabled>
                </td>
            </tr>
            <tr>
                <td>总金额：</td>
                <td><input type="text" name="totalsum"></td>
            </tr>
            <tr>
                <td>定金金额：</td>
                <td><input type="text" name="summoney"></td>
            </tr>
            <tr>
                <td>摘要：</td>
                <td><input type="text" name="intro"></td>
            </tr>
            <tr>
                <td>附件：</td>
                <td>
                    <input id="pic" type="file" name="pic" accept="image/*"/>
                </td>
            </tr>

        </table>
    </form>
</div>

<!-- 审核对话框 -->
<div id="orders_check_dialog">
    <input type="hidden" name="id" id="checkId">
    <table align="center" style="margin-top: 18px">
        <tr>
            <td>定金客户：</td>
            <td><input type="text" name="customer.name" readonly></td>
        </tr>
        <tr>
            <td>签订时间：</td>
            <td><input type="text" name="signtime" readonly></td>
        </tr>
        <tr>
            <td>销售人员姓名：</td>
            <td>
                <input type="text" name="seller.username" readonly/>
            </td>
        </tr>
        <tr>
            <td>总金额：</td>
            <td><input type="text" name="totalsum" readonly></td>
        </tr>
        <tr>
            <td>定金金额：</td>
            <td><input type="text" name="summoney" readonly></td>
        </tr>
        <tr>
            <td>摘要：</td>
            <td><input type="text" name="intro" readonly></td>
        </tr>
        <tr>
            <td>附件：</td>
            <td>
                <input type="text" name="file" readonly/>
                <a class="easyui-linkbutton" iconCls="icon-save" id="download">下载</a>
            </td>
        </tr>
    </table>
</div>
<!-- 对话框底部按钮 -->
<div id="orders_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>

<!-- 审核对话框底部按钮 -->
<div id="orders_check_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="permit">审核通过</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="reject">审核拒绝</a>
</div>
<script src="js/views/orders.js"></script>
</body>
</html>
