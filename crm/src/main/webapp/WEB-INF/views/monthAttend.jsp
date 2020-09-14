<%@ page import="com.crm.domain.Employee" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>月考勤导出</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="monthAttend_datagrid"></table>

<!-- 工具栏按钮 -->
<div id="monthAttend_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.MonthAttendController:save')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.MonthAttendController:update')}">
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.MonthAttendController:remove')}">
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.MonthAttendController:output')}">
            <a iconCls="icon-redo" class="easyui-linkbutton" plain="true" data-cmd="output">导出</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.MonthAttendController:send')}">
            <a iconCls="icon-print" class="easyui-linkbutton" plain="true" data-cmd="send">发送财务部</a>
        </c:if>
    </div>
    <div>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.MonthAttendController:output')}">
            员工ID：<input type="text" name="eid">
        </c:if>
        年份：<select id="sl_year" class="easyui-combobox" name="year" style="width:100px;"
                   data-options="panelHeight:'auto'">
        <option value=''></option>
        <c:forEach begin="2017" end="<%= Calendar.getInstance().get(Calendar.YEAR)%>" var="i">
            <option value='${i}'>${i}</option>
        </c:forEach>
    </select>
        月份：<select id="sl_month" class="easyui-combobox" name="month" style="width:60px;"
                   data-options="panelHeight:'250px'">
        <option value=''></option>
        <c:forEach begin="1" end="12" var="i">
            <option value='${i}'>${i}</option>
        </c:forEach>
    </select>
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">查询</a>
    </div>
</div>


<!-- 新增/更新对话框 -->
<div id="monthAttend_dialog">
    <form id="monthAttend_form" method="post">
        <%--  隐含域，判断签到还是签退 --%>
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>用户ID</td>
                <td>
                    <input type="text" name="emp.id"/>
                </td>
            </tr>
            <tr>
                <td>日期</td>
                <td><input type="text" name="outputtime" class="easyui-datebox"/></td>
            </tr>
            <tr>
                <td>该月出勤天数</td>
                <td><input type="text" name="monthAttendCount"/></td>
            </tr>
            <tr>
                <td>该月迟到天数</td>
                <td><input type="text" name="monthLateCount"/></td>
            </tr>
            <tr>
                <td>该月早退天数</td>
                <td><input type="text" name="monthLeaveEarlyCount"/></td>
            </tr>
        </table>
    </form>
</div>

<!-- 对话框底部按钮 -->
<div id="monthAttend_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>

<%-- 导出对话框 --%>
<div id="output_dialog">
    <form id="output_form" action="monthAttend_output">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>年份：</td>
                <td>
                    <select class="easyui-combobox" name="year" style="width:100px;"
                            data-options="panelHeight:'auto'">
                        <option value=''></option>
                        <c:forEach begin="2017" end="<%= Calendar.getInstance().get(Calendar.YEAR)%>" var="i">
                            <option value='${i}'>${i}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>月份：</td>
                <td>
                    <select class="easyui-combobox" name="month" style="width:60px;"
                            data-options="panelHeight:'250px'">
                        <option value=''></option>
                        <c:forEach begin="1" end="12" var="i">
                            <option value='${i}'>${i}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 导出对话框底部按钮 -->
<div id="output_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="outputExcel">导出</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="outputCancel">取消</a>
</div>

<%-- 发送对话框 --%>
<div id="send_dialog">
    <form id="send_form" method="post" enctype="multipart/form-data">
        上传文件：<input type="file" name="file" accept=".xls,.xlsx">
    </form>
</div>

<!-- 发送对话框底部按钮 -->
<div id="send_dialog_tb">
    <a iconCls="icon-undo" class="easyui-linkbutton" plain="true" data-cmd="sendExcel">发送</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="sendCancel">取消</a>
</div>
<script src="js/views/monthAttend.js"></script>
</body>
</html>
