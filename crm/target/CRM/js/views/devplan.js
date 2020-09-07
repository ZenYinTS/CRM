$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var devplanDatagrid , devplanDatagridId , devplanDialog , devplanForm , devplanCid,
        clearField,emptyField,inputTime,customerId;
    devplanDatagrid = $("#devplan_datagrid");
    devplanDatagridId = $("#id");
    devplanDialog = $("#devplan_dialog");
    devplanForm = $("#devplan_form");
    devplanCid = $("#cid");
    clearField = $("#id,[name='plansubject'],[name='remark']");
    emptyField = $("#pCustomerId,#fCustomerId,#ptId,#result");
    inputTime = $("[name='inputtime']");
    customerId = $("#pCustomerId,#fCustomerId")

    //分页
    var type = $("#type").val();
    var nametitle;
    if(type == 'true')
        nametitle="正式客户名称";
    else
        nametitle="潜在客户名称";
    devplanDatagrid.datagrid({
        fit:true,
        url:'devplan_list?type='+type,
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#devplan_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'customer',align:'center',title:nametitle,width:100,formatter:customerFormatter},
            {field:'plansubject',align:'center',title:"计划主题",width:100},
            {field:'plantype',align:'center',title:"实施方式",width:100,formatter:typeFormatter},
            {field:'traceresult',align:'center',title:"跟进效果",width:100,formatter:resultFormatter},
            {field:'remark',align:'center',title:"备注",width:100},
            {field:'inputuser',align:'center',title:"创建人",width:100,formatter:empFormatter},
            {field:'inputtime',align:'center',title:"创建时间",width:100}
        ]]
    });

    //对话框
    devplanDialog.dialog({
        width:350,
        height:350,
        buttons:"#devplan_dialog_tb",
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
            customerId.combobox('readonly',false);
            //打开对话框
            devplanDialog.dialog("open");
            //设置对话框标题
            devplanDialog.dialog("setTitle","新增");
            //清空表单中的内容
            clearField.val("");
            emptyField.combobox("clear");
        },

        //保存
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = devplanDatagridId.val();
            var url;
            if (idVal){
                url = "devplan_update";
            }else {
                url="devplan_save";
            }
            //发送异步请求
            devplanForm.form("submit",{
                url:url,
                onSubmit:function(param){
                    param["inputtime"] = inputTime.val();
                },
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            devplanDialog.dialog("close");
                            //刷新页面
                            devplanDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },

        cancel:function () {
            devplanDialog.dialog("close");
        },

        refresh:function () {
            devplanDatagrid.datagrid("reload");
        },
        edit:function () {
            customerId.combobox('readonly',true);
            //获取选中的数据
            var rowData = devplanDatagrid.datagrid("getSelected");
            if (rowData){
                //清空表单中的内容
                clearField.val("");
                //打开对话框
                devplanDialog.dialog("open");
                //设置对话框标题
                devplanDialog.dialog("setTitle","编辑");

                //特殊属性的处理：为rowData添加新字段
                if (rowData.customer)
                    rowData["customer.id"] = rowData.customer.id;
                if (rowData.plantype)
                    rowData["plantype.id"] = rowData.plantype.id;
                if (rowData.inputuser)
                    rowData["inputuser.id"] = rowData.inputuser.id;

                devplanDialog.form("load",rowData);   //基于同名匹配规则
            }else {
                $.messager.alert("温馨提示","请选择一条需要编辑的数据","info");
            }
        },

        remove:function () {
            //获取选中的数据
            var rowData = devplanDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要删除这条数据吗？",function (yes) {
                    if (yes){
                        $.get("devplan_remove?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        devplanDatagrid.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条移要删除的数据","info");
            }
        },
        searchBtn:function () {
            var cid = devplanCid.combobox("getValue");
            var startTime = $("#startTime").datebox("getValue");
            var endTime = $("#endTime").datebox("getValue");
            var type = $("#type").val();

            devplanDatagrid.datagrid("load",{
                cid:cid,
                startTime:startTime,
                endTime:endTime,
                type:type
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

function typeFormatter(value,record,index) {
    return value?value.name:value;
}

function resultFormatter(value,record,index) {
    if (value == 3)
        return "<span style='color: green'>优</span>";
    if (value == 2)
        return "<span style='color: black'>中</span>";
    if (value == 1)
        return "<span style='color: red'>差</span>";
}
