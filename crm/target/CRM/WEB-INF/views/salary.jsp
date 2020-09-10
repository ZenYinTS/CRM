<%@ page import="com.crm.domain.Employee" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>工资管理</title>
    <%@include file="common.jsp" %>
</head>
<body>
<table id="salary_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="salary_datagrid_tb">
    <div>
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
        员工：<input type="text" name="keyWord">
        <a class="easyui-linkbutton" iconCls="icon-search" onclick="searchBtn()">查询</a>
    </div>
    <div>
        <%-- 按钮权限控制 --%>
        <c:if test="${myFn:checkPermission('com.crm.web.controller.SalaryController:upload')}">
            <form action="salary_upload" method="post" enctype="multipart/form-data">
                上传工资表单：<input type="file" name="file" accept=".xls,.xlsx"/>
                <button type="submit">提交</button>
            </form>
        </c:if>
    </div>
</div>
<script src="js/views/salary.js"></script>
</body>
</html>
