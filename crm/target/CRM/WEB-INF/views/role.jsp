<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>角色管理</title>
    <%@include file="common.jsp"%>
</head>
<body>
    <table id="role_datagrid"></table>
    <!-- 工具栏按钮 -->
    <div id="role_datagrid_tb">
        <div>
            <c:if test="${myFn:checkPermission('com.crm.web.controller.RoleController:ALL')}">
                <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
                <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
                <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">删除</a>
            </c:if>
            <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        </div>
        <div>
            关键字查询：<input type="text" name="keyWord">
            <a class="easyui-linkbutton" iconCls="icon-search" data-cmd="searchBtn">搜索</a>
        </div>
    </div>

    <!-- 新增/更新对话框 -->
    <div id="role_dialog">
        <form id="role_form" method="post">
            <%--  隐含域，判断新增还是更新 --%>
            <input type="hidden" name="id">
            <table align="center" style="margin-top: 18px">
                <tr>
                    <td>角色名称<input type="text" name="name"></td>
                    <td>角色编号<input type="text" name="sn"></td>
                </tr>
                <tr>
                    <td><table id="allPermission"></table></td>
                    <td><table id="selfPermission"></table></td>
                </tr>
            </table>
        </form>
    </div>
    <!-- 对话框底部按钮 -->
    <div id="role_dialog_tb">
        <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
        <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
    </div>
    <script src="js/views/role.js"></script>
</body>
</html>
