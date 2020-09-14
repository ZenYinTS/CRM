$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var roleDatagrid , roleDialog , roleForm , roleFormId , roleKeyWord,allPermission,selfPermission;
    roleDatagrid = $("#role_datagrid");
    roleDialog = $("#role_dialog");
    roleForm = $("#role_form");
    roleFormId = $("#role_form [name='id']");
    roleKeyWord = $("[name='keyWord']");
    allPermission = $("#allPermission");
    selfPermission = $("#selfPermission");

    //分页
    roleDatagrid.datagrid({
        fit:true,
        url:"role_list",
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#role_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'sn',align:'center',title:"角色编号",width:100},
            {field:'name',align:'center',title:"角色名称",width:100}
        ]]
    });

    //所有权限表的配置
    allPermission.datagrid({
        width:340,
        height:300,
        url:"permission_list",
        title:"所有的权限",
        fitColumns:true,
        rownumbers:true,
        pagination:true,    //所有权限数量比较多，做分页，已有权限较少，不用分页
        singleSelect:true,
        columns:[[
            {field:'sn',align:'center',title:"权限名",width:10}
        ]],
        onDblClickRow:function (rowIndex,rowData) {
            //判断是否已经存在已有权限表中，若存在则选中，不存在则添加
            var rows = selfPermission.datagrid('getRows');
            var flag = true;    //若选中的记录不在已有权限中，可以进行添加操作则为true，反之为false
            var index;
            for (var i=0;i< rows.length;i++){
                if (rows[i].id == rowData.id){
                    flag = false;
                    index = i;
                    break;
                }
            }
            if (flag)
                selfPermission.datagrid('appendRow',rowData);
            else
                selfPermission.datagrid('selectRow',index);
        }
    });

    //所有权限表的分页配置
    var pager = allPermission.datagrid("getPager");
    pager.pagination({
        showPageList:false,
        showRefresh:false,
        displayMsg:''
    });

    //已有权限表的配置
    selfPermission.datagrid({
        width:340,
        height:300,
        title:"已有权限",
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        columns:[[
            {field:'sn',align:'center',title:"权限名",width:10}
        ]],
        //双击删除
        onDblClickRow:function (rowIndex,rowData) {
            selfPermission.datagrid('deleteRow',rowIndex);
        }
    });

    //对话框
    roleDialog.dialog({
        width:700,
        height:450,
        buttons:"#role_dialog_tb",
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
        //新增角色
        add:function () {
            //打开对话框
            roleDialog.dialog("open");
            //设置对话框标题
            roleDialog.dialog("setTitle","新增");
            //清空表单中的内容
            $("[name='id'],[name='sn'],[name='name']").val("");
            //清空已有权限表中的内容
            //方式一：遍历表中的内容，逐条删除
            //方式二：加载空数据（如下所示。rows是必须的字段，默认会加载rows，不写会找不到rows，报错）
            selfPermission.datagrid("loadData",{rows:[]});
        },

        //保存角色
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = roleFormId.val();
            var url;
            if (idVal){
                url = "role_update"
            }else {
                url="role_save";
            }
            //发送异步请求
            roleForm.form("submit",{
                url:url,
                //携带额外参数返回，将已有权限表中的权限返回
                onSubmit:function(param){
                    var rows = selfPermission.datagrid("getRows");
                    for (var i = 0;i<rows.length;i++)
                        param["permissions["+i+"].id"] = rows[i].id;
                },
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            roleDialog.dialog("close");
                            //刷新页面
                            roleDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            })
        },

        cancel:function () {
            roleDialog.dialog("close");
        },

        edit:function () {
            //获取选中的数据
            var rowData = roleDatagrid.datagrid("getSelected");
            if (rowData){
                //打开对话框
                roleDialog.dialog("open");
                //设置对话框标题
                roleDialog.dialog("setTitle","编辑");
                //清空表单中的内容
                $("[name='id'],[name='sn'],[name='name']").val("");
                selfPermission.datagrid("loadData",{rows:[]});
                //对话框传入选中的数据
                //特殊属性处理，请求中传入当前角色的id
                var options = selfPermission.datagrid("options");
                options.url = "permissions_queryByRid";
                selfPermission.datagrid("load",{
                    rid:rowData.id
                });
                roleDialog.form("load",rowData);   //基于同名匹配规则，下拉框要求传入是dept.id
            }else {
                $.messager.alert("温馨提示","请选择一条需要编辑的数据","info");
            }
        },

        remove:function () {
            //获取选中的数据
            var rowData = roleDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要删除这条数据吗？",function (yes) {
                    if (yes){
                        $.get("role_remove?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        roleDatagrid.datagrid("reload");
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
            roleDatagrid.datagrid("reload");
        },

        searchBtn:function () {
            var value = roleKeyWord.val();
            roleDatagrid.datagrid("load",{
                keyWord:value
            })
        }
    };

});

