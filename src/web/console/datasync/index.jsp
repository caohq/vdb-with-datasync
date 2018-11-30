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
<body style="overflow: hidden;">

<h1>datasync登录</h1>
<hr />

<form action="" method="post">
    <label for="userName">用户名：</label>
    <input type="text" id="userName" name="userName"/>
    <br />
    <label for="userName">密&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
    <input type="text" id="password" name="password" />
    <br />
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" id="reset" name="reset"/>
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" id="submit" name="submit"/>
</form>

</body>
</html>