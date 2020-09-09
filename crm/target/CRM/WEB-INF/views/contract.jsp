<%@ page import="com.crm.domain.Employee" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>合同管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="contract_datagrid"></table>
<input type="hidden" id="type" name="type" value="${type}">
<!-- 工具栏按钮 -->
<div id="contract_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.ContractController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.ContractController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.ContractController:remove')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.ContractController:check')}">
            <a id="btnCheck" iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="check">审核</a>
        </c:if>
    </div>
    <div>
        关键字：<input type="text" name="keyWord">
        <select id="contractStatus" class="easyui-combobox" name="status" style="width:100px;"
                data-options="panelHeight:'auto'">
            <option value="">全部</option>
            <option value="0">初始录入</option>
            <option value="1">审核通过</option>
            <option value="2">审核拒绝</option>
        </select>
        开始日期：<input id="startTime" type="text" class="easyui-datebox" name="startTime">
        结束日期：<input id="endTime" type="text" class="easyui-datebox" name="endTime">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn"></a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="contract_dialog">
    <form id="contract_form" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>合同客户：</td>
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
                <td>销售人员名字：</td>
                <td>
                    <input type="hidden" class="easyui-combobox" name="seller.id"
                           value="<%= ((Employee)UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId()%>">
                    <input type="text" class="easyui-combobox" name="seller.username"
                           value="<%= ((Employee)UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getUsername()%>"
                           disabled>
                </td>
            </tr>
            <tr>
                <td>合同金额：</td>
                <td><input type="text" name="summmoney"></td>
            </tr>
            <tr>
                <td>付款金额：</td>
                <td><input type="text" name="money"></td>
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
<div id="contract_check_dialog">
    <input type="hidden" name="id" id="checkId">
    <table align="center" style="margin-top: 18px">
        <tr>
            <td>合同客户：</td>
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
            <td>合同金额：</td>
            <td><input type="text" name="summoney" readonly></td>
        </tr>
        <tr>
            <td>付款金额：</td>
            <td><input type="text" name="money" readonly></td>
        </tr>
        <tr>
            <td>付款时间：</td>
            <td><input type="text" name="signtime" readonly></td>
        </tr>
        <tr>
            <td>合同摘要：</td>
            <td><input type="text" name="paytime" readonly></td>
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
<div id="contract_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>

<!-- 审核对话框底部按钮 -->
<div id="contract_check_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="permit">审核通过</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="reject">审核拒绝</a>
</div>
<script src="js/views/contract.js"></script>
</body>
</html>
