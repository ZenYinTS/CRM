$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var customerDatagrid , customerFailed , customerDialog , customerForm , customerFormId ,
        customerKeyWord,clearField,inputTime,handOverDialog,cNameInput,inchargeNameInput,
        inchargeUserInput,handOverForm,customerStatus;
    customerDatagrid = $("#customer_datagrid");
    customerFailed = $("[iconCls='icon-edit'],[iconCls='icon-remove'],[iconCls='icon-tip'],[iconCls='icon-cancel'],#formalBtn");
    customerDialog = $("#customer_dialog");
    customerForm = $("#customer_form");
    customerFormId = $("#customer_form [name='id']");
    customerKeyWord = $("[name='keyWord']");
    customerStatus = $("#sl_status");
    clearField = $("[name='name'],[name='age'],[name='gender'],[name='tel'],[name='email'],[name='qq'],[name='wechat'],[name='job.id'],[name='salarylevel.id'],[name='customersource.id']");
    inputTime = $("[name = 'inputtime']");
    handOverDialog = $("#customer_handOver_dialog");
    cNameInput = $("#cname");
    inchargeNameInput = $("#inchargeName");
    inchargeUserInput = $("#inchargeuserId");
    handOverForm = $("#customer_handOver_form");


    //分页
    var type = $("#type").val();
    customerDatagrid.datagrid({
        fit:true,
        url:'customer_list?type='+type,
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#customer_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'id',align:'center',title:"编号",width:100},
            {field:'name',align:'center',title:"客户姓名",width:100},
            {field:'age',align:'center',title:'年龄',width:100},
            {field:'gender',align:'center',title:'性别',width:100,formatter:genderFormatter},
            {field:'tel',align:'center',title:'电话号码',width:100},
            {field:'email',align:'center',title:'邮箱',width:100},
            {field:'qq',align:'center',title:'QQ',width:100},
            {field:'wechat',align:'center',title:'微信',width:100},
            {field:'job',align:'center',title:'职业',width:100,formatter:detailFormatter},
            {field:'salarylevel',align:'center',title:'收入水平',width:100,formatter:detailFormatter},
            {field:'customersource',align:'center',title:'客户来源',width:100,formatter:detailFormatter},
            {field:'inchargeuser',align:'center',title:'负责人',width:100,formatter:empFormatter},
            {field:'inputuser',align:'center',title:'创建人',width:100,formatter:empFormatter},
            {field:'inputtime',align:'center',title:'日期',width:100},
            {field:'status',align:'center',title:'状态',width:100,formatter:statusFormatter}
        ]],
        onClickRow:function (rowIndex,rowData) {
            if (rowData.status>=0){
                customerFailed.linkbutton("enable");
            }else {
                customerFailed.linkbutton("disable");


            }
        }
    });


    //对话框
    customerDialog.dialog({
        width:300,
        height:400,
        buttons:"#customer_dialog_tb",
        closed:true
    });


    //对所有携带了data-cmd的按钮进行监听
    $("a[data-cmd]").on("click",function () {
        //获取data-cmd携带的值
        var cmd = $(this).data("cmd");
        if (cmd)
            cmdObj[cmd]();
    });


    //移交对话框
    handOverDialog.dialog({
        width:300,
        height:200,
        buttons:"#handOver_dialog_tb",
        closed:true
    });
    /*---- cmdObj一定要放在加载页面的函数中，否则无法使用抽取出来的变量 ----*/
    var cmdObj = {
        //新增员工
        add:function () {
            //打开对话框
            customerDialog.dialog("open");
            //设置对话框标题
            customerDialog.dialog("setTitle","新增");
            //清空表单中的内容
            clearField.val("");
        },


        //保存员工
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = customerFormId.val();
            var url;
            if (idVal){
                url = "customer_update";
            }else {
                url="customer_save";
            }
            //发送异步请求
            customerForm.form("submit",{
                url:url,
                onSubmit:function(param){
                    param["inputtime"] = inputTime.val();
                },
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            customerDialog.dialog("close");
                            //刷新页面
                            customerDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },


        cancel:function () {
            customerDialog.dialog("close");
        },


        edit:function () {
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                //打开对话框
                customerDialog.dialog("open");
                //设置对话框标题
                customerDialog.dialog("setTitle","编辑");
                //清空表单中的内容
                clearField.val("");


                //特殊属性的处理：为rowData添加新字段
                if (rowData.job)
                    rowData["job.id"] = rowData.job.id;
                if (rowData.salarylevel)
                    rowData["salarylevel.id"] = rowData.salarylevel.id;
                if (rowData.customersource)
                    rowData["customersource.id"] = rowData.customersource.id;
                if (rowData.inputuser)
                    rowData["inputuser.id"] = rowData.inputuser.id;
                if (rowData.inchargeuser)
                    rowData["inchargeuser.id"] = rowData.inchargeuser.id;
                customerDialog.form("load",rowData);   //基于同名匹配规则
            }else {
                $.messager.alert("温馨提示","请选择一条需要编辑的数据","info");
            }
        },

        move:function () {
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要将这条数据移入资源池吗？",function (yes) {
                    if (yes){
                        $.get("customer_move?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        customerDatagrid.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条移入资源池的数据","info");
            }
        },
        lose:function () {
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要操作这条数据吗？",function (yes) {
                    if (yes){
                        $.get("customer_lose?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        customerDatagrid.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条需要流失的数据","info");
            }
        },
        remove:function () {
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要操作这条数据吗？",function (yes) {
                    if (yes){
                        $.get("customer_remove?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        customerDatagrid.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条需要操作的数据","info");
            }
        },
        share:function(){
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要共享这条数据吗？",function (yes) {
                    if (yes){
                        $.get("customer_share?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        customerDatagrid.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条需要共享的数据","info");
            }
        },
        handOver:function(){
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                handOverDialog.dialog("open");
                //设置对话框标题
                handOverDialog.dialog("setTitle","客户移交");
                //清空表单中的内容
                cNameInput.val("");
                inchargeNameInput.val("");
                inchargeUserInput.combobox("clear");

                //属性赋值
                inchargeNameInput.val(rowData.inchargeuser.username + ' - ' + rowData.inchargeuser.realname);
                cNameInput.val(rowData.name);
                $("#cid").val(rowData.id);
                handOverDialog.form("load",rowData);   //基于同名匹配规则
                var sellUrl = "sell_all?inchargeId="+ rowData.inchargeuser.id;
                inchargeUserInput.combobox("reload",sellUrl);
            }else {
                $.messager.alert("温馨提示","请选择一条需要移交的数据","info");
            }
        },
        handOverSave:function () {
            //发送异步请求
            handOverForm.form("submit",{
                url:"customer_handOver",
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            handOverDialog.dialog("close");
                            //刷新页面
                            customerDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },
        handOverCancel:function () {
            handOverDialog.dialog("close");
        },
        formal:function(){
            //获取选中的数据
            var rowData = customerDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要操作这条数据吗？",function (yes) {
                    if (yes){
                        $.get("customer_formal?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        customerDatagrid.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条需要操作的数据","info");
            }
        },
        refresh:function () {
            customerDatagrid.datagrid("reload");
        },


        searchBtn:function () {
            var value = customerKeyWord.val();
            var status = customerStatus.val();
            customerDatagrid.datagrid("load",{
                keyWord:value,
                status:status
            })
        }
    };


});


/*---- 以下三个格式化方法不可以包含在cmdObj中，否则由于不携带data-cmd数据，无法调用 ----*/
/**
 * 将部门字段显示格式化的方法
 * @param value    原来的值
 * @param record    对应的当前记录
 * @param index    索引
 */
function detailFormatter(value,record,index) {
    return value?value.name:value;
}
function statusFormatter (value,record,index) {
    if (value == -2)
        return "流失";
    if (value == -1)
        return '<span style="color: red">开发失败</span>';
    if (value == 0)
        return '<span style="color: blue">潜在客户</span>';
    if (value == 1)
        return '<span style="color: green">正式客户</span>';
    if (value == 2)
        return '<span style="color: orange">资源池客户</span>';
}
function genderFormatter (value,record,index) {
    if(value==1)
        return '男';
    if(value==0)
        return '女';
}


function empFormatter(value,record,index) {
    return value?value.username + ' - ' + value.realname :value;
}