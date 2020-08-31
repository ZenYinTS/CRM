$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var deptDatagrid , deptEditAndRemove , deptDialog , deptForm , deptFormId , deptKeyWord , cbManager , cbParent;
    deptDatagrid = $("#dept_datagrid");
    deptEditAndRemove = $("[iconCls='icon-edit'],[iconCls='icon-remove']");
    deptDialog = $("#dept_dialog");
    deptForm = $("#dept_form");
    deptFormId = $("#dept_form [name='id']");
    deptKeyWord = $("[name='keyWord']");
    cbManager = $("#cbManager");
    cbParent = $("#cbParent");

    //分页
    deptDatagrid.datagrid({
        fit:true,
        url:'dept_list',
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#dept_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'sn',align:'center',title:"部门编号",width:100},
            {field:'name',align:'center',title:"部门名称",width:100},
            {field:'manager',align:'center',title:'部门经理',width:100,formatter:empFormatter},
            {field:'parent',align:'center',title:'上级部门',width:100,formatter:deptFormatter},
            {field:'state',align:'center',title:'状态',width:100,formatter:stateFormatter}
            ]],
        onClickRow:function (rowIndex,rowData) {
            //正常
            if (rowData.state){
                deptEditAndRemove.linkbutton("enable");
            }else {
                //停用
                deptEditAndRemove.linkbutton("disable");

            }
        }
    });

    //对话框
    deptDialog.dialog({
        width:300,
        height:320,
        buttons:"#dept_dialog_tb",
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
        //新增部门
        add:function () {
            //打开对话框
            deptDialog.dialog("open");
            //设置对话框标题
            deptDialog.dialog("setTitle","新增");
            //情空表单中的内容
            deptForm.form("clear");
        },

        //保存部门
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = deptFormId.val();
            var url;
            if (idVal){
                url = "dept_update"
            }else {
                url="dept_save";
            }
            //发送异步请求
            deptForm.form("submit",{
                url:url,
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            deptDialog.dialog("close");
                            //刷新页面
                            deptDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },

        cancel:function () {
            deptDialog.dialog("close");
        },

        edit:function () {
            //获取选中的数据
            var rowData = deptDatagrid.datagrid("getSelected");
            if (rowData){
                //打开对话框
                deptDialog.dialog("open");
                //设置对话框标题
                deptDialog.dialog("setTitle","编辑");
                //情空表单中的内容
                deptForm.form("clear");
                //对话框传入选中的数据
                // console.log(rowData);
                //特殊属性的处理：
                if (rowData.manager)
                    rowData["manager.id"] = rowData.manager.id;
                if (rowData.parent)
                    rowData["parent.id"] = rowData.parent.id;
                deptDialog.form("load",rowData);   //基于同名匹配规则
                var empUrl = "emp_queryForDept?id="+ $('#id').val();
                var deptUrl = "dept_queryForDept?id="+ $('#id').val();
                cbManager.combobox('reload',empUrl);
                cbParent.combobox('reload',deptUrl);
            }else {
                $.messager.alert("温馨提示","请选择一条需要编辑的数据","info");
            }
        },

        remove:function () {
            //获取选中的数据
            var rowData = deptDatagrid.datagrid("getSelected");
            if (rowData){
                $.messager.confirm("温馨提示","您确定要删除这条数据吗？",function (yes) {
                    if (yes){
                        $.get("dept_remove?id="+rowData.id,
                            function (data) {
                                if (data.success){
                                    $.messager.alert("温馨提示",data.msg,"info",function () {
                                        //刷新数据表格
                                        deptDatagrid.datagrid("reload");
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
            deptDatagrid.datagrid("reload");
        },

        searchBtn:function () {
            var value = deptKeyWord.val();
            deptDatagrid.datagrid("load",{
                keyWord:value
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
function deptFormatter(value,record,index) {
    return value?value.name:value;
}
function stateFormatter (value,record,index) {
    return value?'<span style="color: green">正常</span>':'<span style="color: red">停用</span>';
}
function empFormatter (value,record,index) {
    return value?value.username+" - "+value.realname:value;
}

