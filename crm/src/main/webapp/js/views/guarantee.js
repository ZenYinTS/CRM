$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var guaranteeDatagrid,guaranteeItemDatagrid, guaranteeDialog,guaranteeItemDialog,
        guaranteeForm,guaranteeItemForm,guaranteeKeyWord;
    guaranteeDatagrid = $("#guarantee_datagrid");
    guaranteeItemDatagrid = $("#guarantee_item_datagrid");
    guaranteeDialog = $("#guarantee_dialog");
    guaranteeItemDialog = $("#guarantee_item_dialog");
    guaranteeForm = $("#guarantee_form");
    guaranteeItemForm = $("#guarantee_item_form");
    guaranteeKeyWord = $("[name = 'keyWord']");

    //分页
    var gid;
    guaranteeDatagrid.datagrid({
        fit: true,
        url: 'guarantee_list',
        fitColumns: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageList: [1, 5, 10, 20],
        toolbar: '#guarantee_datagrid_tb',
        columns: [[
            /* 宽度随便设置 */
            {field: 'id', align: 'center', title: "编号", width: 100},
            {field: 'sn', align: 'center', title: "保修单号", width: 100},
            {field: 'productname', align: 'center', title: "产品名称", width: 100},
            {field: 'duetime', align: 'center', title: "质保到期时间", width: 100},
            {field: 'remark', align: 'center', title: "备注", width: 100},
            {field: 'customer', align: 'center', title: "保修客户", width: 100, formatter: customerFormatter}
            ]],
        onClickRow: function (rowIndex, rowData) {
            gid = rowData.id;
            guaranteeItemDatagrid.datagrid("reload","guarantee_item_list?gid="+rowData.id);
        }
    });

    guaranteeItemDatagrid.datagrid({
        fit: true,
        fitColumns: true,
        rownumbers: true,
        singleSelect: true,
        toolbar: '#guarantee_item_datagrid_tb',
        columns: [[
            /* 宽度随便设置 */
            {field: 'id', align: 'center', title: "编号", width: 100},
            {field: 'content', align: 'center', title: "保修内容", width: 100},
            {field: 'guaranteetime', align: 'center', title: "保修时间", width: 100},
            {field: 'price', align: 'center', title: "维修费用", width: 100, formatter: priceFormatter},
            {field: 'guarantee', align: 'center', title: "保修单号", width: 100, formatter: snFormatter},
            {field: 'status', align: 'center', title: "保修状态", width: 100, formatter: statusFormatter}
        ]]
    });

    //保修单对话框
    guaranteeDialog.dialog({
        width: 320,
        height: 250,
        buttons: "#guarantee_dialog_tb",
        closed: true
    });
    //保修单明细对话框
    guaranteeItemDialog.dialog({
        width: 350,
        height: 280,
        buttons: "#guarantee_item_dialog_tb",
        closed: true
    });

    //对所有携带了data-cmd的按钮进行监听
    $("a[data-cmd]").on("click", function () {
        //获取data-cmd携带的值
        var cmd = $(this).data("cmd");
        if (cmd)
            cmdObj[cmd]();
    });

    /*---- cmdObj一定要放在加载页面的函数中，否则无法使用抽取出来的变量 ----*/
    var cmdObj = {
        //1. 保修单
        //新增权限
        add: function () {
            //打开对话框
            guaranteeDialog.dialog("open");
            //设置对话框标题
            guaranteeDialog.dialog("setTitle", "新增");
            //清空表单中的内容
            guaranteeForm.form("clear");
        },

        //保存权限
        save: function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = $("[name = 'id']").val();
            var url;
            if (idVal) {
                url = "guarantee_update";
            } else {
                url = "guarantee_save" ;
            }
            // 表单的提交操作
            guaranteeForm.form("submit", {
                url: url,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg, "info");
                        guaranteeDialog.dialog("close");
                        guaranteeDatagrid.datagrid("load");
                    } else {
                        $.messager.alert("温馨提示", data.msg, "warning");
                    }
                }
            });
        },
        edit: function () {
            //获取选中的数据
            var rowData = guaranteeDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                guaranteeDialog.dialog("open");
                //设置对话框标题
                guaranteeDialog.dialog("setTitle", "编辑");
                //清空表单中的内容
                guaranteeForm.form("clear");

                if (rowData.customer)
                    rowData["customer.id"] = rowData.customer.id;
                guaranteeDialog.form("load", rowData);   //基于同名匹配规则
            } else {
                $.messager.alert("温馨提示", "请选择一条需要编辑的数据", "info");
            }
        },
        remove: function () {
            //获取选中的数据
            var rowData = guaranteeDatagrid.datagrid("getSelected");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除这条数据吗？", function (yes) {
                    if (yes) {
                        $.get("guarantee_remove?id=" + rowData.id,
                            function (data) {
                                if (data.success) {
                                    $.messager.alert("温馨提示", data.msg, "info", function () {
                                        //刷新数据表格
                                        guaranteeDatagrid.datagrid("reload");
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
            guaranteeDialog.dialog("close");
        },

        refresh: function () {
            guaranteeDatagrid.datagrid("reload");
        },

        //2. 保修单明细
        //新增权限
        itemAdd: function () {
            //打开对话框
            guaranteeItemDialog.dialog("open");
            //设置对话框标题
            guaranteeItemDialog.dialog("setTitle", "新增");
            //清空表单中的内容
            guaranteeItemForm.form("clear");
        },

        //保存权限
        itemSave: function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = $("[name = 'id']").val();
            var url;
            if (idVal) {
                url = "guarantee_item_update";
            } else {
                url = "guarantee_item_save?gid="+ gid;
            }
            // 表单的提交操作
            guaranteeItemForm.form("submit", {
                url: url,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg, "info");
                        guaranteeItemDialog.dialog("close");
                        guaranteeItemDatagrid.datagrid("load");
                    } else {
                        $.messager.alert("温馨提示", data.msg, "warning");
                    }
                }
            });
        },
        itemEdit: function () {
            //获取选中的数据
            var rowData = guaranteeItemDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                guaranteeItemDialog.dialog("open");
                //设置对话框标题
                guaranteeItemDialog.dialog("setTitle", "编辑");
                //清空表单中的内容
                guaranteeItemForm.form("clear");
                guaranteeItemDialog.form("load", rowData);   //基于同名匹配规则
                if (rowData.status){
                    $('#rb_continue').removeAttr("checked");
                    $('#rb_finish').prop("checked","checked");
                }else {
                    $('#rb_continue').prop("checked","checked");
                    $('#rb_finish').removeAttr("checked");
                }
            } else {
                $.messager.alert("温馨提示", "请选择一条需要编辑的数据", "info");
            }
        },
        itemRemove: function () {
            //获取选中的数据
            var rowData = guaranteeItemDatagrid.datagrid("getSelected");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除这条数据吗？", function (yes) {
                    if (yes) {
                        $.get("guarantee_item_remove?id=" + rowData.id,
                            function (data) {
                                if (data.success) {
                                    $.messager.alert("温馨提示", data.msg, "info", function () {
                                        //刷新数据表格
                                        guaranteeItemDatagrid.datagrid("reload");
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
        itemCancel: function () {
            guaranteeItemDialog.dialog("close");
        },

        itemRefresh: function () {
            guaranteeItemDatagrid.datagrid("reload");
        },

        searchBtn: function () {
            var keyWord = guaranteeKeyWord.val();
            guaranteeDatagrid.datagrid("load", {keyWord: keyWord})
        }

    };

});

function snFormatter(value, record, index) {
    return value ? value.sn: value;
}

function customerFormatter(value, record, index) {
    return value ? value.name : value;
}

function statusFormatter(value, record, index) {
    if (value == 0) {
        return "<span style='color: red'>进行</span>";
    }
    if (value == 1) {
        return "<span style='color: green'>完成</span>";
    }
}

function priceFormatter(value, record, index) {
   return value == 0?"<span style='color: red'>免费维修</span>":value;
}