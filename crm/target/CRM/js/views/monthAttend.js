$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var monthAttendDatagrid , monthAttendDialog , monthAttendForm , monthAttendFormId,
        outputDialog , outputForm;
    monthAttendDatagrid = $("#monthAttend_datagrid");
    monthAttendDialog = $("#monthAttend_dialog");
    monthAttendForm = $("#monthAttend_form");
    monthAttendFormId = $("#monthAttend_form [name='id']");
    outputDialog = $("#output_dialog");
    outputForm = $("#output_form");

    //分页
    monthAttendDatagrid.datagrid({
        fit:true,
        url:'monthAttend_list',
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#monthAttend_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'id',align:'center',title:"编号",width:100},
            {field:'emp',align:'center',title:"用户ID",width:100,formatter:empFormatter},
            {field:'outputtime',align:'center',title:"日期",width:100},
            {field:'monthAttendCount',align:'center',title:"该月出勤天数",width:100},
            {field:'monthLateCount',align:'center',title:"该月迟到天数",width:100},
            {field:'monthLeaveEarlyCount',align:'center',title:"该月早退天数",width:100}
        ]]
    });

    //对话框
    monthAttendDialog.dialog({
        width:350,
        height:250,
        buttons:"#monthAttend_dialog_tb",
        closed:true
    });

    //导出对话框
    outputDialog.dialog({
        width:250,
        height:200,
        buttons:"#output_dialog_tb",
        closed:true
    });

    //对所有携带了data-cmd的按钮进行监听
    $("a[data-cmd]").on("click",function () {
        //获取data-cmd携带的值
        var cmd = $(this).data("cmd");
        if (cmd)
            cmdObj[cmd]();
    });

    /*---- cmdObj一定要放在加载页面的函数中，否则无法使用抽取出来的变量 ----*/
    var cmdObj = {
        //新增权限
        add:function () {
            //打开对话框
            monthAttendDialog.dialog("open");
            //设置对话框标题
            monthAttendDialog.dialog("setTitle","新增");
            //清空表单中的内容
            monthAttendForm.form("clear");
        },

        edit:function () {
            //获取选中的数据
            var rowData = monthAttendDatagrid.datagrid("getSelected");
            if (rowData) {
                //打开对话框
                monthAttendDialog.dialog("open");
                //设置对话框标题
                monthAttendDialog.dialog("setTitle", "编辑");
                //清空表单中的内容
                monthAttendForm.form("clear");

                if (rowData.emp)
                    rowData["emp.id"] = rowData.emp.id;
                monthAttendDialog.form("load", rowData);   //基于同名匹配规则
            } else {
                $.messager.alert("温馨提示", "请选择一条需要编辑的数据", "info");
            }
        },

        //保存权限
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = monthAttendFormId.val();
            var url;
            if (idVal){
                url = "monthAttend_update"
            }else {
                url="monthAttend_save";
            }
            //发送异步请求
            monthAttendForm.form("submit",{
                url:url,
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            monthAttendDialog.dialog("close");
                            //刷新页面
                            monthAttendDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },
        remove: function () {
            //获取选中的数据
            var rowData = monthAttendDatagrid.datagrid("getSelected");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除这条数据吗？", function (yes) {
                    if (yes) {
                        $.get("monthAttend_remove?id=" + rowData.id,
                            function (data) {
                                if (data.success) {
                                    $.messager.alert("温馨提示", data.msg, "info", function () {
                                        //刷新数据表格
                                        monthAttendDatagrid.datagrid("reload");
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
        cancel:function () {
            monthAttendDialog.dialog("close");
        },

        refresh:function () {
            monthAttendDatagrid.datagrid("reload");
        },
        searchBtn: function () {
            var eid = $("[name='eid']").val();
            var year = $("#sl_year").combobox("getValue");
            var month = $("#sl_month").combobox("getValue");
            monthAttendDatagrid.datagrid("load", {
                eid: eid,
                year: year,
                month: month
            })
        },

        output:function () {
            //打开对话框
            outputDialog.dialog("open");
            //设置对话框标题
            outputDialog.dialog("setTitle","导出");
            //清空表单中的内容
            outputForm.form("clear");
        },

        outputExcel:function () {
            outputForm.form("submit");
        }
    };


});
function empFormatter(value, record, index) {
    return value ? value.id: value;
}