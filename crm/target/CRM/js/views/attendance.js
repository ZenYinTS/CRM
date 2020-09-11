$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var attendanceDatagrid , attendanceDialog , attendanceForm , attendanceFormId,
        attendanceResignDialog , attendanceResignForm,attendanceResignFormId;
    attendanceDatagrid = $("#attendance_datagrid");
    attendanceDialog = $("#attendance_dialog");
    attendanceResignDialog = $("#attendance_resign_dialog");
    attendanceForm = $("#attendance_form");
    attendanceResignForm = $("#attendance_resign_form");
    attendanceFormId = $("#attendance_form [name='id']");
    attendanceResignFormId = $("#attendance_resign_form [name='id']");

    //分页
    var columnEmp = [
        /* 宽度随便设置 */
        {field:'id',align:'center',title:"编号",width:100},
        {field:'emp',align:'center',title:"姓名",width:100,formatter:empFormatter},
        {field:'ip',align:'center',title:"签到ip",width:100},
        {field:'signintime',align:'center',title:"签到时间",width:100},
        {field:'signouttime',align:'center',title:"签退时间",width:100},
        {field:'status',align:'center',title:"状态",width:100,formatter:statusFormatter}
    ];
    if ($("#isHR").val()=="true"){
        var hrColumn = [
            {field:'resignemp',align:'center',title:"补签人",width:100,formatter:empFormatter},
            {field:'resigntime',align:'center',title:"补签时间",width:100}
        ];
        columnEmp = columnEmp.concat(hrColumn);
    }
    attendanceDatagrid.datagrid({
        fit:true,
        url:'attendance_list',
        fitColumns:true,
        rownumbers:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#attendance_datagrid_tb',
        columns:[columnEmp]
    });

    //对话框
    attendanceDialog.dialog({
        width:350,
        height:250,
        buttons:"#attendance_dialog_tb",
        closed:true
    });

    //补签对话框
    attendanceResignDialog.dialog({
        width:350,
        height:250,
        buttons:"#attendance_resign_dialog_tb",
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
        signIn:function () {
            //打开对话框
            attendanceDialog.dialog("open");
            //设置对话框标题
            attendanceDialog.dialog("setTitle","签到");
            //清空表单中的内容
            attendanceForm.form("clear");
            $("[name = 'emp.username']").val($("#username").val());
            $("[name='signintime']").val(formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        },

        signOut:function () {
            //打开对话框
            attendanceDialog.dialog("open");
            //设置对话框标题
            attendanceDialog.dialog("setTitle","签退");
            //情空表单中的内容
            attendanceForm.form("clear");
            $.ajax({
                url:"queryForSignInfo",
                type:"POST",
                success:function (data) {
                    data["emp.username"] = data.emp.username;
                    data["signouttime"] = formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
                    attendanceForm.form("load",data);
                },
                dataType:"json"
            });
        },

        //保存权限
        save:function () {
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = attendanceFormId.val();
            var url;
            if (idVal){
                url = "attendance_signOut"
            }else {
                url="attendance_signIn";
            }
            //发送异步请求
            attendanceForm.form("submit",{
                url:url,
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            attendanceDialog.dialog("close");
                            //刷新页面
                            attendanceDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },

        cancel:function () {
            attendanceDialog.dialog("close");
        },

        resign:function(){
            //打开对话框
            attendanceResignDialog.dialog("open");
            //设置对话框标题
            attendanceResignDialog.dialog("setTitle", "补签");
            //清空表单中的内容
            attendanceResignForm.form("clear");

            //获取选中的数据
            var rowData = attendanceDatagrid.datagrid("getSelected");
            if (rowData) {
                rowData["emp.id"] = rowData.emp.id;
                attendanceResignForm.form("load", rowData);   //基于同名匹配规则
            }
        },

        resignSave:function(){
            //使用id字段是否为空的方法进行判断，新增时，隐含域中id为空，更新时，自动注入id，id非空
            var idVal = attendanceResignFormId.val();
            var url;
            if (idVal){
                url = "attendance_resign?type=update"
            }else {
                url="attendance_resign?type=insert";
            }
            //发送异步请求
            attendanceResignForm.form("submit",{
                url:url,
                success:function (data) {
                    data = $.parseJSON(data);
                    if (data.success){
                        $.messager.alert("温馨提示",data.msg,"info",function () {
                            //关闭对话框
                            attendanceResignDialog.dialog("close");
                            //刷新页面
                            attendanceDatagrid.datagrid("load");
                        });
                    }else {
                        $.messager.alert("温馨提示",data.msg,"info");
                    }
                }
            });
            return false;
        },

        resignCancel:function () {
            attendanceResignDialog.dialog("close");
        },

        refresh:function () {
            attendanceDatagrid.datagrid("reload");
        }
    };

});
function empFormatter(value, record, index) {
    return value ? value.username + ' - ' + value.realname : value;
}

function statusFormatter(value, record, index) {
    if (value == 0)
        return "<span style='color: green'>正常</span>";
    if (value == 1)
        return "<span style='color: blue'>迟到</span>";
    if (value == 2)
        return "<span style='color: red'>早退</span>";
}

function formatDate(date, format) {
    var v = "";
    if (typeof date == "string" || typeof date != "object") {
        return;
    }
    var year  = date.getFullYear();
    var month  = date.getMonth()+1;
    var day   = date.getDate();
    var hour  = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();

    v = format;
    //Year
    v = v.replace(/yyyy/g, year);
    v = v.replace(/YYYY/g, year);
    v = v.replace(/yy/g, (year+"").substring(2,4));
    v = v.replace(/YY/g, (year+"").substring(2,4));

    //Month
    var monthStr = ("0"+month);
    v = v.replace(/MM/g, monthStr.substring(monthStr.length-2));

    //Day
    var dayStr = ("0"+day);
    v = v.replace(/dd/g, dayStr.substring(dayStr.length-2));

    //hour
    var hourStr = ("0"+hour);
    v = v.replace(/HH/g, hourStr.substring(hourStr.length-2));
    v = v.replace(/hh/g, hourStr.substring(hourStr.length-2));

    //minute
    var minuteStr = ("0"+minute);
    v = v.replace(/mm/g, minuteStr.substring(minuteStr.length-2));

    //second
    var secondStr = ("0"+second);
    v = v.replace(/ss/g, secondStr.substring(secondStr.length-2));
    v = v.replace(/SS/g, secondStr.substring(secondStr.length-2));
    return v;
}


