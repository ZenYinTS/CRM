<%@ page import="com.crm.domain.Employee" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>（潜在）客户开发计划</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="devplan_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="devplan_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.DevplanController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">添加</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.DevplanController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.DevplanController:remove')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
        </c:if>

        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
    <div>
        客户：
        <c:if test="${!type}">
            <input type="text" class="easyui-combobox" id="cid" name="cid" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'potential_customer',
                    panelHeight:'auto'
                    ">
        </c:if>
        <c:if test="${type}">
            <input type="text" class="easyui-combobox" id="cid" name="cid" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'formal_customer',
                    panelHeight:'auto'
                    ">
        </c:if>
        时间段查询：<input id="startTime" type="text" class="easyui-datebox" name="startTime">
        - <input id="endTime" type="text" class="easyui-datebox" name="endTime">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">查询</a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="devplan_dialog">
    <form id="devplan_form" method="post">
        <input id="id" name="id" type="hidden"/>
        <input id="type" name="type" type="hidden" value="${type}"/>
        <table align="center" style="margin-top: 18px">
            <tr>
                <c:if test="${!type}">
                    <td>潜在客户：</td>
                    <td><input id="pCustomerId" type="text" class="easyui-combobox" name="customer.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'potential_customer',
                    panelHeight:'auto'
                    "></td>
                </c:if>
                <c:if test="${type}">
                    <td>正式客户：</td>
                    <td><input id="fCustomerId" type="text" class="easyui-combobox" name="customer.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'formal_customer',
                    panelHeight:'auto'
                    "></td>
                </c:if>

            </tr>
            <tr>
                <td>计划主题：</td>
                <td><input type="text" name="plansubject"></td>
            </tr>
            <tr>
                <td>实施方式：</td>
                <td><input id="ptId" type="text" class="easyui-combobox" name="plantype.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'listPlanType',
                    panelHeight:'auto'
                    "></td>
            </tr>
            <tr>
                <td>跟进效果：</td>
                <td>
                    <select id="result" class="easyui-combobox" name="traceresult" style="width:80px;"
                            data-options="panelHeight:'auto',editable:false,
                    onLoadSuccess:function () {
                        $(this).combobox('clear');
                    }">
                        <option value="3">优</option>
                        <option value="2">中</option>
                        <option value="1">差</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td><input type="text" name="remark"></td>
            </tr>
            <tr>
                <td>创建人</td>
                <td>
                    <input type="hidden" name="inputuser.id"
                           value="<%= ((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId()%>"/>
                    <input type="text" disabled
                           value="<%= ((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getUsername()%>"/>
                </td>
            </tr>
            <tr>
                <td>创建时间</td>
                <td>
                    <input type="text" name="inputtime"
                           value="<%= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>" disabled/>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 对话框底部按钮 -->
<div id="devplan_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>
<script src="js/views/devplan.js"></script>
</body>
</html>
