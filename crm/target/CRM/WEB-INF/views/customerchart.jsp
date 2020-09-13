<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>客户新增报表</title>
    <%@include file="common.jsp" %>
</head>
<body>
<input type="hidden" id="type" value="${type}">
<table id="chart_datagrid"></table>
<!-- 工具栏按钮 -->
<div id="chart_datagrid_tb">
    <div>
        名称关键字查询：
        <input type="text" name="keyWord">

        时间段查询：<input id="startTime" type="text" class="easyui-datebox" name="startTime">
        - <input id="endTime" type="text" class="easyui-datebox" name="endTime">
        <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">查询</a>
        <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        分组信息：
        <select id="sl_group" class="easyui-combobox" name="groupInfo" style="width:100px;"
                data-options="panelHeight:'auto'">
            <option value="year">年份</option>
            <option value="quarter">季度</option>
            <option value="month">月份</option>
        </select>
        <a class="easyui-linkbutton" iconCls="icon-tip" data-cmd="displayChart">生成图表</a>
    </div>
</div>
<script src="js/views/customerchart.js"></script>
</body>
</html>
