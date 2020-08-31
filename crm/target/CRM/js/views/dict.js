$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var catalog , detail , dictDialog , dictForm , dictFormId;
    catalog = $("#catalog");
    detail = $("#detail");
    dictForm = $("#dict_form");
    dictFormId = $("#dict_form [name='id']");
    dictDialog = $("#dict_dialog")

    detail.datagrid({
        fit:true,
        fitColumns:true,
        singleSelect : true,
        toolbar:'#dict_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'sn',align:'center',title:"字典明细编号",width:100},
            {field:'name',align:'center',title:"字典明细名称",width:100},
            {field:'intro',align:'center',title:"字典明细简介",width:100},
            {field:'parent',align:'center',title:"字典目录",width:100,formatter:dictFormatter}
        ]]
    });

    catalog.datagrid({
        fit:true,
        url:"catalog_list",
        fitColumns:true,
        singleSelect : true,
        columns:[[
            /* 宽度随便设置 */
            {field:'sn',align:'center',title:"字典编号",width:100},
            {field:'name',align:'center',title:"字典名称",width:100},
            {field:'intro',align:'center',title:"字典简介",width:100}
        ]],
        onSelect:function (rowIndex,rowData) {
            var url = "detail_list?pid="+rowData.id;
            detail.datagrid("reload",url);
        },
        onLoadSuccess:function () {
            //设置默认选中第一行
            catalog.datagrid("selectRow",0);
        }
    });
    
    //对话框
    dictDialog.dialog({
        width:300,
        height:200,
        buttons:"#dict_dialog_tb",
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
        //新增字典
        add:function () {
            //打开对话框
            dictDialog.dialog("open");
            //设置对话框标题
            dictDialog.dialog("setTitle","新增");
            //清空表单中的内容
            dictForm.form("clear");
        },

        //保存字典
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = dictFormId.val();
            var url;
            if (idVal){
                url = "dict_update"
            }else {
                url="dict_save";
            }
            //发送异步请求
            dictForm.form("submit",{
                url:url,
                //携带额外参数返回，将父节点id返回
                onSubmit:function(param){
                    var parent = catalog.datagrid("getSelected");
                    param["parent.id"] = parent.id;
                    console.log(parent.id);
                },
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            dictDialog.dialog("close");
                            //刷新页面
                            detail.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            })
        },

        cancel:function () {
            dictDialog.dialog("close");
        },

        edit:function () {
            //获取选中的数据
            var rowData = detail.datagrid("getSelected");
            if (rowData){
                //打开对话框
                dictDialog.dialog("open");
                //设置对话框标题
                dictDialog.dialog("setTitle","编辑");
                //清空表单中的内容
                dictForm.form("clear");
                //对话框传入选中的数据
                //特殊属性处理，请求中传入当前字典的id
                if (rowData.parent)
                    rowData["parent.id"] = rowData.parent.id;
                dictDialog.form("load",rowData);   //基于同名匹配规则，下拉框要求传入是parent.id
            }else {
                $.messager.alert("温馨提示","请选择一条需要编辑的数据","info");
            }
        },

        remove:function () {
            //获取选中的数据
            var rowData = detail.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要删除这条数据吗？",function (yes) {
                    if (yes){
                        $.get("dict_remove?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        detail.datagrid("reload");
                                    });
                                }else {
                                    $.messager.alert("温馨提示",data.msg,"info");
                                }
                            },"json")
                    }
                });
            }else {
                $.messager.alert("温馨提示","请选择一条需要删除的数据","info");
            }
        },

        refresh:function () {
            detail.datagrid("reload");
        }
    };
});

function dictFormatter(value,record,index) {
    return value?value.name:value;
}

