<%@ page import="com.crm.domain.Employee" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>售后管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
    <div data-options="region:'center',title:'保修单',split:true" style="height:50%">
        <table id="guarantee_datagrid"></table>
    </div>
    <div data-options="region:'south',title:'保修单明细',split:true" style="height:50%;">
        <table id="guarantee_item_datagrid"></table>
    </div>
</div>
<!-- 保修单工具栏按钮 -->
<div id="guarantee_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.GuaranteeController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.GuaranteeController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.GuaranteeController:remove')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
    <div>
        关键字：<input type="text" name="keyWord">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn"></a>
    </div>
</div>

<!-- 保修单明细工具栏按钮 -->
<div id="guarantee_item_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.GuaranteeItemController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="itemAdd">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.GuaranteeItemController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="itemEdit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.GuaranteeItemController:remove')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="itemRemove">删除</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="itemRefresh">刷新</a>
    </div>
</div>

<!-- 保修单新增/更新对话框 -->
<div id="guarantee_dialog">
    <form id="guarantee_form" method="post">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>产品名称：</td>
                <td><input type="text" name="productname"></td>
            </tr>
            <tr>
                <td>保修客户：</td>
                <td><input type="text" class="easyui-combobox" name="customer.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'customer_contract',
                    panelHeight:'auto'
                    "></td>
            </tr>
            <tr>
                <td>质保到期时间：</td>
                <td><input type="text" name="duetime" readonly></td>
            </tr>
            <tr>
                <td>备注：</td>
                <td><input type="text" name="remark"></td>
            </tr>
        </table>
    </form>
</div>

<!-- 保修单明细新增/更新对话框 -->
<div id="guarantee_item_dialog">
    <form id="guarantee_item_form" method="post">
        <input type="hidden" name="id"/>
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>保修时间：</td>
                <td><input type="text" class="easyui-datebox" name="guaranteetime"></td>
            </tr>
            <tr>
                <td>保修内容：</td>
                <td><input type="text" name="content"></td>
            </tr>
            <tr>
                <td>维修费用：</td>
                <td><input type="text" name="price"></td>
            </tr>
            <tr>
                <td>保修状态：</td>
                <td>
                    <input type="radio" id="rb_continue" name="status" value="0" checked>进行
                    <input type="radio" id="rb_finish" name="status" value="1">完成
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 保修单对话框底部按钮 -->
<div id="guarantee_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>

<!-- 保修单明细对话框底部按钮 -->
<div id="guarantee_item_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="itemSave">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="itemCancel">取消</a>
</div>

<script src="js/views/guarantee.js"></script>
</body>
</html>
