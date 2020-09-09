$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var contractDatagrid, contractDialog, contractForm, clearField, clearCombobox,
        clearDatebox, contractKeyWord, contractStatus, contractCheckDialog, btnCheck, btnDCheck, btnFCheck, typeValue;
    contractDatagrid = $("#contract_datagrid");
    contractDialog = $("#contract_dialog");
    contractForm = $("#contract_form");
    clearField = $("[name='money'],[name='summoney'],[name='intro'],[name='id']");
    clearCombobox = $("#customerId");
    clearDatebox = $("#signtime");
    contractKeyWord = $("[name = 'keyWord']");
    contractStatus = $("#contractStatus");
    contractCheckDialog = $("#contract_check_dialog");
    btnCheck = $("#btnCheck");

    //分页
    contractDatagrid.datagrid({
        fit: true,
        url: 'contract_list',
        fitColumns: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageList: [1, 5, 10, 20],
        toolbar: '#contract_datagrid_tb',
        columns: [[
            /* 宽度随便设置 */
            {field: 'sn', align: 'center', title: "编号", width: 100},
            {field: 'customer', align: 'center', title: "合同客户", width: 100, formatter: customerFormatter},
            {field: 'signtime', align: 'center', title: "签约时间", width: 100},
            {field: 'seller', align: 'center', title: "销售人员名字", width: 100, formatter: empFormatter},
            {field: 'summoney', align: 'center', title: "合同金额", width: 100},
            {field: 'money', align: 'center', title: "付款金额", width: 100},
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
            } else if (rowData.status == 1 || rowData.status == 2) {
                btnCheck.linkbutton("disable");
                btnDCheck.linkbutton("disable");
                btnFCheck.linkbutton("disable");
            }
        }
    });

    //对话框
    contractDialog.dialog({
        width: 400,
        height: 320,
        buttons: "#contract_dialog_tb",
        closed: true
    });

    //审核对话框
    contractCheckDialog.dialog({
        width: 400,
        height: 320,
        buttons: "#contract_check_dialog_tb",
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
            contractDialog.dialog("open");
            //设置对话框标题
            contractDialog.dialog("setTitle", "新增");
            //清空表单中的内容
            clearForm();
        },

        //保存权限
        save: function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = $("[name = 'id']").val();
            var url;
            if (idVal) {
                url = "contract_update";
            } else {
                url = "contract_save" ;
            }
            // 表单的提交操作
            contractForm.form("submit", {
                url: url,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg, "info");
                        contractDialog.dialog("close");
                        contractDatagrid.datagrid("load");
                    } else {
                        $.messager.alert("温馨提示", data.msg, "warning");
                    }
                }
            });
        },
        edit: function () {
            //获取选中的数据
            var rowData = contractDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                contractDialog.dialog("open");
                //设置对话框标题
                contractDialog.dialog("setTitle", "编辑");
                //清空表单中的内容
                clearForm();

                if (rowData.customer)
                    rowData["customer.id"] = rowData.customer.id;
                if (rowData.seller)
                    rowData["seller.id"] = rowData.seller.id;
                if (rowData.modifyuser)
                    rowData["modifyuser.id"] = rowData.modifyuser.id;
                contractDialog.form("load", rowData);   //基于同名匹配规则
            } else {
                $.messager.alert("温馨提示", "请选择一条需要编辑的数据", "info");
            }
        },
        remove: function () {
            //获取选中的数据
            var rowData = contractDatagrid.datagrid("getSelected");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除这条数据吗？", function (yes) {
                    if (yes) {
                        $.get("contract_remove?id=" + rowData.id,
                            function (data) {
                                if (data.success) {
                                    $.messager.alert("温馨提示", data.msg, "info", function () {
                                        //刷新数据表格
                                        contractDatagrid.datagrid("reload");
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
            contractDialog.dialog("close");
        },

        refresh: function () {
            contractDatagrid.datagrid("reload");
        },

        searchBtn: function () {
            var keyWord = contractKeyWord.val();
            var status = contractStatus.combobox("getValue");
            var startTime = $("#startTime").datebox("getValue");
            var endTime = $("#endTime").datebox("getValue");
            contractDatagrid.datagrid("load", {
                keyWord: keyWord,
                status: status,
                startTime: startTime,
                endTime: endTime
            })
        },

        check: function () {
            //获取选中的数据
            var rowData = contractDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                contractCheckDialog.dialog("open");
                //设置对话框标题
                contractCheckDialog.dialog("setTitle", "审核");
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
                    $("#download").attr("href", "contract_download?path=" + rowData.file);
                }
                contractCheckDialog.form("load", rowData);   //基于同名匹配规则
            } else {
                $.messager.alert("温馨提示", "请选择一条需要审核的数据", "info");
            }
        },

        permit: function () {
            $.messager.confirm("温馨提示", "您确定要审核通过这条数据吗？", function (yes) {
                if (yes) {
                    $.get("contract_check?option=permit&id=" + $("#checkId").val(),
                        function (data) {
                            if (data.success) {
                                $.messager.alert("温馨提示", data.msg, "info", function () {
                                    //刷新数据表格
                                    contractDatagrid.datagrid("reload");
                                });
                            } else {
                                $.messager.alert("温馨提示", data.msg, "info");
                            }
                        }, "json")
                }
                contractCheckDialog.dialog("close");
            });
        },
        reject: function () {
            $.messager.confirm("温馨提示", "您确定要审核拒绝这条数据吗？", function (yes) {
                if (yes) {
                    $.get("contract_check?option=reject&id=" + $("#checkId").val(),
                        function (data) {
                            if (data.success) {
                                $.messager.alert("温馨提示", data.msg, "info", function () {
                                    //刷新数据表格
                                    contractDatagrid.datagrid("reload");
                                });
                            } else {
                                $.messager.alert("温馨提示", data.msg, "info");
                            }
                        }, "json")
                }
                contractCheckDialog.dialog("close");
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
        return "<span style='color: yellow'>审核通过</span>";
    }
    if (value == 2) {
        return "<span style='color: pink'>审核拒绝</span>";

    }

}