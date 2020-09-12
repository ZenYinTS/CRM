$(function () {
    //1. 抽取变量:每次都是从上而下的搜索组件，重复的组件会多次搜索，抽取变量一次搜索，减少访问页面的次数
    //2. 所有方法统一管理
    //3. 统一处理按钮监听
    var salaryDatagrid = $("#salary_datagrid");

    //分页
    salaryDatagrid.datagrid({
        fit: true,
        url: 'salary_list',
        fitColumns: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageList: [1, 5, 10, 20],
        toolbar: '#salary_datagrid_tb',
        columns: [[
            /* 宽度随便设置 */
            {field: 'id', align: 'center', title: "编号", width: 100},
            {field: 'year', align: 'center', title: "年份", width: 100},
            {field: 'month', align: 'center', title: "月份", width: 100},
            {field: 'salary', align: 'center', title: "工资", width: 100},
            {field: 'emp', align: 'center', title: "员工", width: 100, formatter: empFormatter}
            ]]
    });
});

function empFormatter(value, record, index) {
    return value ? value.username + ' - ' + value.realname : value;
}

function searchBtn() {
    var keyWord = $("[name='keyWord']").val();
    var year = $("#sl_year").combobox("getValue");
    var month = $("#sl_month").combobox("getValue");
    $("#salary_datagrid").datagrid("load", {
        keyWord: keyWord,
        year: year,
        month: month
    })
}

function submit() {
    $("#salaryForm").form("submit");
}