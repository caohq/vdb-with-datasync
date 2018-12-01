<%@ page  language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="vdb.mydb.*" %>
<%@ page import="vdb.mydb.engine.VdbEngine" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>登陆页面</title>
    <script type="text/javascript" src="/console/shared/js/jquery-3.2.1.min.js " ></script>
    <script src="/console/shared/bootstrap-3.3.7/js/bootstrap.js"></script>
    <script src="/console/datasync/js/dragdiv.js"></script>
    <style>
        .login_in_div{
            text-align: center;
            position: absolute;
            top: 85%;
            left: 50%;
            height: 30%;
            width: 50%;
            margin: -15% 0 0 -25%;
        }

    </style>
</head>
<body style="overflow: hidden;margin: 0;">
<div>
  <div class="backgrond-image" style="position: relative;background-image: url('/console/datasync/vdbNewLog/images/Log03_bg.jpg');background-repeat-y:no-repeat;height: 100%;width: 100%;background-size: 94%">
     <div class="login_in_div">
         <h1>登&nbsp;&nbsp;&nbsp;&nbsp;录</h1>
         <hr style="width: 60%"/>
         <%--<form action="/login" method="post">--%>
         <%--<form action="" method="post">--%>
            <label for="userName">用户名：</label>
            <input type="text" id="userName" class="" name="userName" style="width: 200px;margin: 10px;"/>
            <br />
            <label for="userName">密&nbsp;&nbsp;&nbsp;码：</label>
            <input type="text" id="password" class="" name="password" style="width: 200px;margin: 10px;" />
            <br />
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" onclick="start()" id="reset" name="reset"/>
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" onclick="start()" id="submit" name="submit"/>
         <%--</form>--%>
     </div>
  </div>
</div>
<script type="text/javascript">

    function start(){
        // window.location.href="/console/datasync/starter.jsp";
       // document.location.href="/j_spring_security_check?j_username=root&j_password=1&j_uri=%2Fcatalog%2F";
      //  window,location.href="/console/datasync/starter.jsp";
        $.ajax({
            type:"POST",
            url:"/j_spring_security_check",
            data:{
                j_username:"root",
                j_password:"1",
                j_uri:"%2Fcatalog%2F"
            },
            success:function () {
                window,location.href="/console/datasync/starter.jsp";
            },
            error:function () {
                console.log("请求失败")
            }
        })
    };

</script>
</body>
</html>