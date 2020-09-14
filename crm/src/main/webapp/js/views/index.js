$(function () {
    $("#menu").tree({
        url:"queryForMenu",
        onClick:function(node){
            addTab(node);
        }
    });
});

function addTab(node){
    if ($('.easyui-tabs').tabs('exists', node.text)) {
        $('.easyui-tabs').tabs('select',node.text)
    } else {
        //将字符串转化为json数据
        //在数据库中设置attributes属性值为{url:'employee'}，在函数中直接调用获取到的是字符串
        if (node.attributes && typeof(node.attributes)=='string'){
            node.attributes = $.parseJSON(node.attributes);
        }
        var content = '<iframe src='+node.attributes.url+' style="height: 100%;width: 100%;" frameborder=0></iframe>';
        $('.easyui-tabs').tabs('add',{
            title:node.text,
            closable:true,
            content:content
        });
    }
}