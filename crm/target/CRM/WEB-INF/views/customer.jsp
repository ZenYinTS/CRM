<%@ page import="com.crm.domain.Employee" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.crm.service.IEmployeeService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>客户管理</title>
    <%@include file="common.jsp" %>

</head>
<body>
<input type="hidden" id="type" value="${type}">
<table id="customer_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="customer_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:takein') && 'resource'.equals(type)}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="takein">吸纳</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:save') && ('formal'.equals(type) || 'potential'.equals(type))}">
            <a id="addBtn" iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:update') && ('formal'.equals(type) || 'potential'.equals(type))}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:move') && 'formal'.equals(type)}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="move">移入资源池</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:share') && ('formal'.equals(type) || 'potential'.equals(type))}">
            <a iconCls="icon-tip" class="easyui-linkbutton" plain="true" data-cmd="share">共享</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:handOver') && ('formal'.equals(type) || 'potential'.equals(type))}">
            <a iconCls="icon-tip" class="easyui-linkbutton" plain="true" data-cmd="handOver">移交</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:remove') && 'potential'.equals(type)}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">开发失败</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:lose') && 'formal'.equals(type)}">
            <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="lose">流失</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:formal') && 'potential'.equals(type)}">
            <a id="formalBtn" iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="formal">转正</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.CustomerController:output') && 'formal'.equals(type)}">
            <a iconCls="icon-redo" class="easyui-linkbutton" plain="true" href="${ctp}/customer_output">导出用户</a>
        </c:if>
    </div>
    <div>
        关键字查询：<input type="text" name="keyWord">
        <c:if test="${!'resourcce'.equals(type)}">
            <select id="sl_status" class="easyui-combobox" name="status" style="width:100px;"
                    data-options="panelHeight:'auto'">
                <c:if test="${'potential'.equals(type)}">
                    <option value="-3">全部</option>
                    <option value="0">潜在客户</option>
                    <option value="-1">开发失败</option>
                </c:if>
                <c:if test="${'formal'.equals(type)}">
                    <option value="-4">全部</option>
                    <option value="1">正式客户</option>
                    <option value="-2">流失客户</option>
                </c:if>
            </select>
        </c:if>
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">搜索</a>
    </div>
</div>

<!-- 新增/更新对话框 -->
<div id="customer_dialog">
    <form id="customer_form" method="post">
        <%--  隐含域，判断新增还是更新 --%>
        <input type="hidden" name="id"/>
        <input type="hidden" name="status"/>
        <input type="hidden" name="formaltime"/>
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>客户姓名</td>
                <td><input type="text" name="name"></td>
            </tr>
            <tr>
                <td>客户年龄</td>
                <td><input type="text" name="age"></td>
            </tr>
            <tr>
                <td>客户性别</td>
                <td>
                    <select id="gender" class="easyui-combobox" name="gender" style="width:100px;" data-options="panelHeight:'auto',editable:false,
                    onLoadSuccess:function () {
                        $(this).combobox('clear');
                    }">
                        <option value="1" selected>男</option>
                        <option value="0">女</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>电话号码</td>
                <td><input type="text" name="tel"></td>
            </tr>
            <tr>
                <td>邮箱</td>
                <td><input type="text" name="email"></td>
            </tr>
            <tr>
                <td>QQ</td>
                <td><input type="text" name="qq"></td>
            </tr>
            <tr>
                <td>微信</td>
                <td><input type="text" name="wechat"></td>
            </tr>
            <tr>
                <td>职业</td>
                <td>
                    <input id="job" type="text" class="easyui-combobox" name="job.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'job_queryInDetail',
                    panelHeight:'auto'
                    ">
                </td>
            </tr>
            <tr>
                <td>收入水平</td>
                <td>
                    <input id="salary" type="text" class="easyui-combobox" name="salarylevel.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'salarylevel_queryInDetail',
                    panelHeight:'auto'
                    ">
                </td>
            </tr>
            <tr>
                <td>客户来源</td>
                <td>
                    <input id="source" type="text" class="easyui-combobox" name="customersource.id" data-options="
                    valueField: 'id',
                    textField: 'name',
                    url:'customersource_queryInDetail',
                    panelHeight:'auto'
                    ">
                </td>
            </tr>
            <tr>
                <td>负责人</td>
                <td>
                    <input type="text" disabled
                           value="<%= ((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getUsername()%>"/>
                </td>
            </tr>
            <tr>
                <td>创建人</td>
                <td>
                    <input type="hidden" name="inputsuer.id"
                           value="<%= ((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getId()%>"/>
                    <input type="text" disabled
                           value="<%= ((Employee) UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getUsername()%>"/>
                </td>
            </tr>
            <tr>
                <td>创建日期</td>
                <td>
                    <input type="text" name="inputtime"
                           value="<%= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>" disabled/>
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 移交对话框 -->
<div id="customer_handOver_dialog">
    <form id="customer_handOver_form" method="post">
        <div>当前潜在客户：</div>
        <input id="cid" type="hidden" name="id"/>
        <input id="cname" type="text" readonly name="name"/>
        <div>当前潜在客户负责人：</div>
        <input id="inchargeName" type="text" disabled/>
        <div>移交给：</div>
        <input id="inchargeuserId" class="easyui-combobox" name="inchargeuser.id" data-options="
            valueField: 'id',
            textField: 'username',
            panelHeight:'auto'
        "/>
    </form>
</div>

<!-- 对话框底部按钮 -->
<div id="customer_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>

<!-- 移交对话框底部按钮 -->
<div id="handOver_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="handOverSave">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="handOverCancel">取消</a>
</div>

<script src="js/views/customer.js"></script>
</body>
</html>
