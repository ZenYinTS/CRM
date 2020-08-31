$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var permissionDatagrid , permissionDialog , permissionForm , permissionFormId , permissionKeyWord;
    permissionDatagrid = $("#permission_datagrid");
    permissionDialog = $("#permission_dialog");
    permissionForm = $("#permission_form");
    permissionFormId = $("#permission_form [name='id']");
    permissionKeyWord = $("[name='keyWord']");

    //分页
    permissionDatagrid.datagrid({
        fit:true,
        url:'permission_list',
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#permission_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'sn',align:'center',title:"权限名称",width:100},
            {field:'resource',align:'center',title:"资源地址",width:100},
            ]]
    });

    //对话框
    permissionDialog.dialog({
        width:300,
        height:150,
        buttons:"#permission_dialog_tb",
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
            permissionDialog.dialog("open");
            //设置对话框标题
            permissionDialog.dialog("setTitle","新增");
            //情空表单中的内容
            permissionForm.form("clear");
        },

        //保存权限
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = permissionFormId.val();
            var url;
            if (idVal){
                url = "permission_update"
            }else {
                url="permission_save";
            }
            //发送异步请求
            permissionForm.form("submit",{
                url:url,
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            permissionDialog.dialog("close");
                            //刷新页面
                            permissionDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },

        cancel:function () {
            permissionDialog.dialog("close");
        },

        edit:function () {
            //获取选中的数据
            var rowData = permissionDatagrid.datagrid("getSelected");
            if (rowData){
                //打开对话框
                permissionDialog.dialog("open");
                //设置对话框标题
                permissionDialog.dialog("setTitle","编辑");
                //情空表单中的内容
                permissionForm.form("clear");
                //对话框传入选中的数据
                permissionDialog.form("load",rowData);   //基于同名匹配规则，下拉框要求传入是dept.id
            }else {
                $.messager.alert("温馨提示","请选择一条需要编辑的数据","info");
            }
        },

        remove:function () {
            //获取选中的数据
            var rowData = permissionDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要删除这条数据吗？",function (yes) {
                    if (yes){
                        $.get("permission_remove?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        permissionDatagrid.datagrid("reload");
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
            permissionDatagrid.datagrid("reload");
        },

        searchBtn:function () {
            var value = permissionKeyWord.val();
            permissionDatagrid.datagrid("load",{
                keyWord:value
            })
        }
    };

});

