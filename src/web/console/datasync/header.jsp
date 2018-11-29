<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Insert title here</title>
    <script type="text/javascript" src="/console/shared/js/jquery-3.2.1.min.js " ></script>
    <link rel="stylesheet" href="/console/shared/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/console/shared/plugins/dhtmlx/layout/skins/dhtmlxlayout_dhx_skyblue.css">
    <script src="/console/shared/bootstrap-3.3.7/js/bootstrap.min.js"></script>
</head>
<style>
    .head{
        float: left;
        background: url(/console/shared/images/top_right_bg.jpg) top left no-repeat;
        background-size:100% 100%;
        width: 25%;
        height: 100%;
    }
    .top_left_div{
        height: 80px;
        width: 403px;
        float: left;
        background: url(/console/shared/images/top_left_bg.jpg) top left no-repeat;
        padding-top: 40px;
        padding-left: 86px;
    }
    .title_left_title_div{
        font-size: 16px;
        font-family: "黑体";
        color: #FFF;
    }
    .foot_div {
        height: 48px;
        width: 100%;
        background: url(/console/shared/images/foot_bg.jpg) repeat-x;
        text-align: center;
        padding-top: 10px;
        color: #FFFFFF;
        position:fixed;
        bottom:0;
    }
    .foot_top {
        height: 10px;
        background: #428BEA;
    }
    body {
        font-family: "宋体";
        font-size: 12px;
    }
</style>
<body>
<div style="width: 100%;height: 80px;">
    <div class="head" style="width: 75%;height: 100%;background-image: url('/console/shared/images/top_mid_bg.jpg');background-repeat:repeat-x;">
        <div class="top_left_div" >
            <div class="title_left_title_div">
                数据管理与共享平台
            </div>

        </div>
        <%--<img src="/console/shared/images/top_left_bg.jpg">--%>
    </div>
    <div  class="head">
    </div>
</div>

<div  id="footDiv" class="foot_div" style="z-index:99999999;" >
    <div id="footTopDiv" class="foot_top" style="height: 10px;" ></div>
    <div class="foot_div" style="height: 38px !important;">
        Powered By 中国科学院计算机网络信息中心·科学数据中心 2018年12月
    </div>
</div>
</body>

</html>