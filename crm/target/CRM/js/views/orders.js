$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var ordersDatagrid, ordersDialog, ordersForm, clearField, clearCombobox,
        clearDatebox, ordersKeyWord, ordersStatus, ordersCheckDialog, btnCheck, btnDCheck, btnFCheck;
    ordersDatagrid = $("#orders_datagrid");
    ordersDialog = $("#orders_dialog");
    ordersForm = $("#orders_form");
    clearField = $("[name='totalsum'],[name='summoney'],[name='intro'],[name='id']");
    clearCombobox = $("#customerId");
    clearDatebox = $("#signtime");
    ordersKeyWord = $("[name = 'keyWord']");
    ordersStatus = $("#orderStatus");
    ordersCheckDialog = $("#orders_check_dialog");
    btnCheck = $("#btnCheck");
    btnDCheck = $("#btnDCheck");
    btnFCheck = $("#btnFCheck");

    //分页
    ordersDatagrid.datagrid({
        fit: true,
        url: 'orders_list',
        fitColumns: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageList: [1, 5, 10, 20],
        toolbar: '#orders_datagrid_tb',
        columns: [[
            /* 宽度随便设置 */
            {field: 'customer', align: 'center', title: "定金客户", width: 100, formatter: customerFormatter},
            {field: 'signtime', align: 'center', title: "签约时间", width: 100},
            {field: 'seller', align: 'center', title: "销售人员姓名", width: 100, formatter: empFormatter},
            {field: 'totalsum', align: 'center', title: "总金额", width: 100},
            {field: 'summoney', align: 'center', title: "定金金额", width: 100},
            {field: 'intro', align: 'center', title: "摘要"},
            {field: 'file', align: 'center', title: "附件"},
            {field: 'modifyuser', align: 'center', title: "最近修改人", width: 100, formatter: empFormatter},
            {field: 'modifytime', align: 'center', title: "最近修改时间", width: 100},
            {field: 'status', align: 'center', title: "状态", width: 100, formatter: statusFormatter}
        ]],
        onClickRow: function (rowIndex, rowData) {
            if (rowData.status == 0) {
                btnCheck.linkbutton("enable");
                btnDCheck.linkbutton("disable");
                btnFCheck.linkbutton("disable");
            } else if (rowData.status == 3 || rowData.status == 4) {
                btnCheck.linkbutton("disable");
                btnDCheck.linkbutton("disable");
                btnFCheck.linkbutton("disable");
            } else if (rowData.status == 1) {
                btnCheck.linkbutton("disable");
                btnDCheck.linkbutton("enable");
                btnFCheck.linkbutton("disable");
            } else if (rowData.status == 2) {
                btnCheck.linkbutton("disable");
                btnDCheck.linkbutton("disable");
                btnFCheck.linkbutton("enable");
            }
        }
    });

    //对话框
    ordersDialog.dialog({
        width: 400,
        height: 320,
        buttons: "#orders_dialog_tb",
        closed: true
    });

    //审核对话框
    ordersCheckDialog.dialog({
        width: 400,
        height: 320,
        buttons: "#orders_check_dialog_tb",
        closed: true
    });

    //对所有携带了data-cmd的按钮进行监听
    $("a[data-cmd]").on("click", function () {
        //获取data-cmd携带的值
        var cmd = $(this).data("cmd");
        if (cmd)
            cmdObj[cmd]();
    });

    function clearForm() {
        clearField.val("");
        clearCombobox.combobox("clear");
        clearDatebox.datebox("clear");
        document.getElementById("pic").outerHTML = document.getElementById("pic").outerHTML;
    }

    /*---- cmdObj一定要放在加载页面的函数中，否则无法使用抽取出来的变量 ----*/
    var cmdObj = {
        //新增权限
        add: function () {
            //打开对话框
            ordersDialog.dialog("open");
            //设置对话框标题
            ordersDialog.dialog("setTitle", "新增");
            //清空表单中的内容
            clearForm();
        },

        //保存权限
        save: function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = $("[name = 'id']").val();
            var url;
            if (idVal) {
                url = "orders_update";
            } else {
                url = "orders_save";
            }
            // 表单的提交操作
            ordersForm.form("submit", {
                url: url,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg, "info");
                        ordersDialog.dialog("close");
                        ordersDatagrid.datagrid("load");
                    } else {
                        $.messager.alert("温馨提示", data.msg, "warning");
                    }
                }
            });
        },
        edit: function () {
            //获取选中的数据
            var rowData = ordersDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                ordersDialog.dialog("open");
                //设置对话框标题
                ordersDialog.dialog("setTitle", "编辑");
                //清空表单中的内容
                clearForm();

                if (rowData.customer)
                    rowData["customer.id"] = rowData.customer.id;
                if (rowData.seller)
                    rowData["seller.id"] = rowData.seller.id;
                if (rowData.modifyuser)
                    rowData["modifyuser.id"] = rowData.modifyuser.id;
                ordersDialog.form("load", rowData);   //基于同名匹配规则
            } else {
                $.messager.alert("温馨提示", "请选择一条需要编辑的数据", "info");
            }
        },
        remove: function () {
            //获取选中的数据
            var rowData = ordersDatagrid.datagrid("getSelected");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除这条数据吗？", function (yes) {
                    if (yes) {
                        $.get("orders_remove?id=" + rowData.id,
                            function (data) {
                                if (data.success) {
                                    $.messager.alert("温馨提示", data.msg, "info", function () {
                                        //刷新数据表格
                                        ordersDatagrid.datagrid("reload");
                                    });
                                } else {
                                    $.messager.alert("温馨提示", data.msg, "info");
                                }
                            }, "json")
                    }
                });
            } else {
                $.messager.alert("温馨提示", "请选择一条需要删除的数据", "info");
            }
        },
        cancel: function () {
            ordersDialog.dialog("close");
        },

        refresh: function () {
            ordersDatagrid.datagrid("reload");
        },

        searchBtn: function () {
            var keyWord = ordersKeyWord.val();
            var status = ordersStatus.combobox("getValue");
            var startTime = $("#startTime").datebox("getValue");
            var endTime = $("#endTime").datebox("getValue");
            ordersDatagrid.datagrid("load", {
                keyWord: keyWord,
                status: status,
                startTime: startTime,
                endTime: endTime
            })
        },

        check: function () {
            //获取选中的数据
            var rowData = ordersDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                ordersCheckDialog.dialog("open");
                //设置对话框标题
                ordersCheckDialog.dialog("setTitle", "审核");
                //清空表单中的内容
                clearForm();

                if (rowData.customer)
                    rowData["customer.name"] = rowData.customer.name;
                if (rowData.seller)
                    rowData["seller.username"] = rowData.seller.username;
                if (rowData.modifyuser)
                    rowData["modifyuser.username"] = rowData.modifyuser.username;
                if (rowData.file) {
                    //为下载按钮分配href
                    $("#download").attr("href", "orders_download?path=" + rowData.file);
                }
                ordersCheckDialog.form("load", rowData);   //基于同名匹配规则
            } else {
                $.messager.alert("温馨提示", "请选择一条需要审核的数据", "info");
            }
        },

        permit: function () {
            $.messager.confirm("温馨提示", "您确定要审核通过这条数据吗？", function (yes) {
                if (yes) {
                    $.get("orders_check?option=permit&id=" + $("#checkId").val(),
                        function (data) {
                            if (data.success) {
                                $.messager.alert("温馨提示", data.msg, "info", function () {
                                    //刷新数据表格
                                    ordersDatagrid.datagrid("reload");
                                });
                            } else {
                                $.messager.alert("温馨提示", data.msg, "info");
                            }
                        }, "json")
                }
                ordersCheckDialog.dialog("close");
            });
        },
        reject: function () {
            $.messager.confirm("温馨提示", "您确定要审核拒绝这条数据吗？", function (yes) {
                if (yes) {
                    $.get("orders_check?option=reject&id=" + $("#checkId").val(),
                        function (data) {
                            if (data.success) {
                                $.messager.alert("温馨提示", data.msg, "info", function () {
                                    //刷新数据表格
                                    ordersDatagrid.datagrid("reload");
                                });
                            } else {
                                $.messager.alert("温馨提示", data.msg, "info");
                            }
                        }, "json")
                }
                ordersCheckDialog.dialog("close");
            });
        }
    };

});

function empFormatter(value, record, index) {
    return value ? value.username + ' - ' + value.realname : value;
}

function customerFormatter(value, record, index) {
    return value ? value.name : value;
}

function statusFormatter(value, record, index) {
    if (value == 0) {
        return "<span style='color: red'>初始录入</span>";
    }
    if (value == 1) {
        return "<span style='color: blue'>部门未审核</span>";
    }
    if (value == 2) {
        return "<span style='color: orange'>财务未审核</span>";
    }
    if (value == 3) {
        return "<span style='color: green'>已生成合同</span>";
    }
    if (value == 4) {
        return "<span style='color: black'>已退款</span>";
    }
}