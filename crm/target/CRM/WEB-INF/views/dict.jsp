<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myFn" uri="http://www.crm.com/crm/permission" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>数据字典管理</title>
    <%@include file="common.jsp"%>
</head>
<body>
    <div class="easyui-layout" style="width:100%;height:100%;">
        <div id="p" data-options="region:'west'" title="字典目录" style="width:50%;">
            <table id="catalog"></table>
        </div>
        <div data-options="region:'center'" title="字典目录明细">
            <table id="detail"></table>
        </div>
    </div>
    <!-- 工具栏按钮 -->
    <div id="dict_datagrid_tb">
        <div>
            <c:if test="${myFn:checkPermission('com.crm.web.controller.DictController:ALL')}">
            <a iconCls="icon-add" class="easyui-linkbutton" plain="true" data-cmd="add">新增</a>
            <a iconCls="icon-edit" class="easyui-linkbutton" plain="true" data-cmd="edit">编辑</a>
            <a iconCls="icon-remove" class="easyui-linkbutton" plain="true" data-cmd="remove">禁用</a>
            </c:if>
            <a iconCls="icon-reload" class="easyui-linkbutton" plain="true" data-cmd="refresh">刷新</a>
        </div>
    </div>

    <!-- 新增/更新对话框 -->
    <div id="dict_dialog">
        <form id="dict_form" method="post">
            <%--  隐含域，判断新增还是更新 --%>
            <input type="hidden" name="id">
            <table align="center" style="margin-top: 18px">
                <tr>
                    <td>字典明细编号</td>
                    <td><input type="text" name="sn"/></td>
                </tr>
                <tr>
                    <td>字典明细名称</td>
                    <td><input type="text" name="name"/></td>
                </tr>
                <tr>
                    <td>字典明细简介</td>
                    <td><input type="text" name="intro"/></td>
                </tr>
            </table>
        </form>
    </div>
    <!-- 对话框底部按钮 -->
    <div id="dict_dialog_tb">
        <a iconCls="icon-save" class="easyui-linkbutton" plain="true" data-cmd="save">保存</a>
        <a iconCls="icon-cancel" class="easyui-linkbutton" plain="true" data-cmd="cancel">取消</a>
    </div>
    <script src="js/views/dict.js"></script>
</body>
</html>
