<%@ page  language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="vdb.mydb.*" %>
<%@ page import="vdb.mydb.engine.VdbEngine" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Insert title here</title>
    <script type="text/javascript" src="/console/shared/js/jquery-3.2.1.min.js " ></script>
    <script src="/console/shared/bootstrap-3.3.7/js/bootstrap.js"></script>
    <script src="/console/datasync/js/dragdiv.js"></script>
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-3.3.7/css/bootstrap-table.min.css">
    <link rel="stylesheet" type="text/css" href="/console/datasync/css/createTask.css" />
    <link rel="stylesheet" type="text/css" href="/console/datasync/css/style.min.css" />
    <style>
        .fixed-table-pagination .page-list{
            display: none !important;
        }
        *{margin:0;padding:0;}
        ul,ol,li{list-style: none;margin-bottom: 0px !important;}
        #wrap{width:100%;height: 91%;position: relative;clear:both;}
        #wrap li{float: left;overflow: hidden;
            text-align: center;line-height: 50px;}
        #wrap li.li1{width: 15%}
        #wrap li.li2{width: 85%}
        #wrap li.li3{width:0px;display: none;}
        #wrap label{float: left;width: 8.8px;height: 90%;
            position: absolute;cursor: e-resize;}
        #wrap label.lab1{background: #438AEB;}
        #wrap label.lab2{}
        a {
            color: #161617 !important;
        }
        .left_title_bg_div {
            height: 46px;
            background: url(/console/shared/images/left_title_bg.jpg) repeat-x;
        }
        .left_title_div {
            height: 46px;
            width: 180px;
            text-align: left;
            background: url(/console/shared/images/left_title.jpg) no-repeat;
            line-height: 28px;
            padding-left: 80px;
            font-size: 16px;
            color: #FFF;
            font-weight: bold;
        }
        .right_top_div {
            background: url(/console/shared/images/position_bg.jpg) repeat-x;
            height: 22px;
        }
        .right_top_left_div {
            background: url(/console/shared/images/position_left.jpg) left no-repeat;
            height: 19px;
            padding-top: 3px;
            padding-left: 12px;
            line-height: 14px !important;
            text-align: left;
        }
    </style>
</head>
<body style="overflow: hidden;">
<div>
    <div id="header">
        <%@ include file="/console/datasync/header.jsp"%>
    </div>

    <div id='wrap'>
        <ul>
            <li class="li1">
                <div id="treeTitle" class="left_title_bg_div">
                    <div class="left_title_div">目录配置</div>
                </div>
                <div style="width: 180px;height:90%;float:left;display: block;margin-left: 14px;margin-top: 20px;vertical-align:middle;">
                    <div style="background-color: #438AEB;height: 30px;text-align: center ;margin-top: 2px;width: 80%;" >
                        <a onclick="goToPage('datatask/dataTask.jsp')" href="javascript:void(0)" style="font-size:20px;text-align:center;display:block;position: relative;top:50%;transform:translateY(-50%);">
                           数据任务
                        </a>
                    </div>
                    <div style="background-color: #438AEB;height: 30px;text-align: center ;margin-top: 2px; width: 80%;" >
                        <a onclick="goToPage('settingtask/settingTask.jsp')" href="javascript:void(0)" style="font-size:20px;text-align:center;display:block;position: relative;top:50%;transform:translateY(-50%);">
                            设置任务
                        </a>
                    </div>
                </div>
            </li>
            <label class="lab1" id='lab1'></label>
            <li class="li2" style="height: 482px;">
                <div  style="float: left;width: 100%;height: 100%;min-height: 300px;overflow:hidden;margin-left: 6px;" >
                <div class="right_top_div" style="width: 100%;height: 5%;">
                    <div class="right_top_left_div">
                        <img src="/console/shared/images/ico_01.gif" height="14" width="20" style="vertical-align: bottom !important;"> <font style="color:#ffffff;font-size:12px">&nbsp;您的当前位置：</font>
                        <font style="color:#fffd4d;font-size:12px"><strong>首页 -&gt; 目录配置</strong></font>
                    </div>
                </div>
                <iframe src="settingtask/settingTask.jsp" width="100%" style="border:none;height: 95%;" id="iframe"></iframe>
                </div>
            </li>
        </ul>
    </div>
</div>

<script type="text/javascript" src="/console/shared/bootstrap-3.3.7/js/bootstrap-table.js"></script>

<script type="text/javascript">

    // 切换iframe页面
      function goToPage(param){
        document.getElementById("iframe").src=param;
        $("#myIframe").attr("src",param);
      }
</script>

</body>
</html>