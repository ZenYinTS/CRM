<%@ page import="com.crm.domain.Employee" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>考勤</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="attendance_datagrid"></table>
<%-- 是否是人事 --%>
<input id="isHR" type="hidden" value="${myFn:checkPermission('com.crm.web.controller.AttendanceController:resign')}"/>

<!-- 工具栏按钮 -->
<div id="attendance_datagrid_tb">
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.AttendanceController:signIn')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="signIn">签到</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.AttendanceController:signOut')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="signOut">签退</a>
        </c:if>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.AttendanceController:resign')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="resign">补签</a>
        </c:if>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
    </div>
</div>

<!-- 签到/签退对话框 -->
<div id="attendance_dialog">
    <input type="hidden" id="username" value="<%= ((Employee)UserContext.get().getSession().getAttribute(UserContext.USERINSESSION)).getUsername()%>">
    <form id="attendance_form" method="post">
        <%--  隐含域，判断签到还是签退 --%>
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>姓名</td>
                <td>
                    <input type="text" name="emp.username" readonly/>
                </td>
            </tr>
            <tr>
                <td>签到ip</td>
                <td><input type="text" name="ip"/></td>
            </tr>
            <tr>
                <td>签到时间</td>
                <td><input type="text" name="signintime" readonly/></td>
            </tr>
            <tr>
                <td>签退时间</td>
                <td><input type="text" name="signouttime" readonly/></td>
            </tr>
        </table>
    </form>
</div>

<!-- 补签对话框 -->
<div id="attendance_resign_dialog">
    <form id="attendance_resign_form" method="post">
        <%--  隐含域，判断签到还是签退 --%>
        <input type="hidden" name="id">
        <table align="center" style="margin-top: 18px">
            <tr>
                <td>补签员工号：</td>
                <td>
                    <input type="text" name="emp.id"/>
                </td>
            </tr>
            <tr>
                <td>签到ip</td>
                <td><input type="text" name="ip"/></td>
            </tr>
            <tr>
                <td>签到时间</td>
                <td><input type="text" class="easyui-datetimebox" name="signintime"/></td>
            </tr>
            <tr>
                <td>签退时间</td>
                <td><input type="text" class="easyui-datetimebox" name="signouttime"/></td>
            </tr>
        </table>
    </form>
</div>

<!-- 对话框底部按钮 -->
<div id="attendance_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">确定</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
</div>

<!-- 补签对话框底部按钮 -->
<div id="attendance_resign_dialog_tb">
    <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="resignSave">补签</a>
    <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="resignCancel">取消</a>
</div>
<script src="js/views/attendance.js"></script>
</body>
</html>
