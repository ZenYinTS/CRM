$(function(){
    var chartDatagrid,keyWord,startTime,endTime,groupInfo,type;
    chartDatagrid = $("#chart_datagrid");
    keyWord = $("[name='keyWord']");
    startTime = $("#startTime");
    endTime = $("#endTime");
    type = $("#type");
    groupInfo = $("#sl_group");

    var nametitle;
    if (type.val()=='potential'){
        nametitle = "潜在客户新增数量";
    }
    if (type.val()=='formal'){
        nametitle = "正式客户新增数量";
    }

    chartDatagrid.datagrid({
        fit:true,
        url:'chart_list?type='+type.val(),
        fitColumns:true,
        rownumbers:true,
        striped:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#chart_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'groupInfo',align:'center',title:"分组信息",width:100},
            {field:'emp',align:'center',title:"员工姓名",width:100,formatter:empFormatter},
            {field:'customerCount',align:'center',title:nametitle,width:100}
        ]]
    });

    $("a[data-cmd]").on("click",function () {
        var cmd = $(this).data("cmd");
        if (cmd)
            cmdObj[cmd]();
    });

    var cmdObj = {
        searchBtn:function () {
            var key = keyWord.val();
            var st = startTime.datebox("getValue");
            var et = endTime.datebox("getValue");
            var gi = groupInfo.combobox("getValue");

            chartDatagrid.datagrid("load",{
                keyWord:key,
                startTime:st,
                endTime:et,
                groupInfo:gi
            });
        },

        refresh:function () {
            chartDatagrid.datagrid("reload");
        },

        displayChart:function () {
            //获得窗口的垂直位置
            var iTop = (window.screen.availHeight - 30 - 400) / 2;
            //获得窗口的水平位置
            var iLeft = (window.screen.availWidth - 10 - 600) / 2;
            window.open("display_chart?groupInfo="+groupInfo.combobox("getValue")+"&type="+type.val(), '_blank','width=600px,height=400px,top=' + iTop + ',left=' + iLeft );
        }
    };
});

function empFormatter(value,record,index) {
    return value?value.username+" - "+value.realname:value;
}
