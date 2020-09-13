<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <%@include file="common.jsp" %>
</head>
<body style="background: white;text-align: center">
<div id="barchart" style="width: 100%;height: 100%;"></div>
<script src="js/echarts.min.js"></script>
<script>
    var barchart =  echarts.init(document.getElementById("barchart"));
    barchart.setOption(${option});
</script>
</body>
</html>
