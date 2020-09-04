$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var transferDatagrid , transferDialog , transferForm , transferCName;
    transferDatagrid = $("#transfer_datagrid");
    transferDialog = $("#transfer_dialog");
    transferForm = $("#transfer_form");
    transferCName = $("[name='cName']");

    //分页
    transferDatagrid.datagrid({
        fit:true,
        url:'transfer_list',
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#transfer_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'customer',align:'center',title:"客户名称",width:100,formatter:customerFormatter},
            {field:'transtime',align:'center',title:"移交时间",width:100},
            {field:'oldseller',align:'center',title:"原市场专员",width:100,formatter:empFormatter},
            {field:'newseller',align:'center',title:"新市场专员",width:100,formatter:empFormatter},
            {field:'transreason',align:'center',title:"移交原因",width:100},
            {field:'transuser',align:'center',title:"操作人",width:100,formatter:empFormatter}
        ]]
    });

    //对话框
    transferDialog.dialog({
        width:350,
        height:200,
        buttons:"#transfer_dialog_tb",
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
            transferDialog.dialog("open");
            //设置对话框标题
            transferDialog.dialog("setTitle","新增");
            //情空表单中的内容
            transferForm.form("clear");
        },

        //保存权限
        save:function () {
            //发送异步请求
            transferForm.form("submit",{
                url:"transfer_save",
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            transferDialog.dialog("close");
                            //刷新页面
                            transferDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },

        cancel:function () {
            transferDialog.dialog("close");
        },

        refresh:function () {
            transferDatagrid.datagrid("reload");
        },

        searchBtn:function () {
            var cname = transferCName.val();
            var startTime = $("#startTime").datebox("getValue");
            var endTime = $("#endTime").datebox("getValue");
            transferDatagrid.datagrid("load",{
                cname:cname,
                startTime:startTime,
                endTime:endTime
            })
        }
    };

});
function empFormatter(value,record,index) {
    return value?value.username + ' - ' + value.realname:value;
}

function customerFormatter(value,record,index) {
    return value?value.name:value;
}
