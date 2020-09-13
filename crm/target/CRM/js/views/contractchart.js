$(function(){
    var chartDatagrid,startTime,endTime,groupInfo;
    chartDatagrid = $("#chart_datagrid");
    startTime = $("#startTime");
    endTime = $("#endTime");
    groupInfo = $("#sl_group");

    chartDatagrid.datagrid({
        fit:true,
        url:'contract_chart_list',
        fitColumns:true,
        rownumbers:true,
        striped:true,
        singleSelect : true,
        pagination:true,
        pageList : [ 1, 5, 10, 20 ],
        toolbar:'#chart_datagrid_tb',
        columns:[[
            /* 宽度随便设置 */
            {field:'groupInfo',align:'center',title:"时间",width:100},
            {field:'money',align:'center',title:"销售金额",width:100}
            ]]
    });

    $("a[data-cmd]").on("click",function () {
        var cmd = $(this).data("cmd");
        if (cmd)
            cmdObj[cmd]();
    });

    var cmdObj = {
        searchBtn:function () {
            var st = startTime.datebox("getValue");
            var et = endTime.datebox("getValue");
            var gi = groupInfo.combobox("getValue");

            chartDatagrid.datagrid("load",{
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
            window.open("display_contract_chart?groupInfo="+groupInfo.combobox("getValue"), '_blank','width=600px,height=400px,top=' + iTop + ',left=' + iLeft );
        }
    };
});
