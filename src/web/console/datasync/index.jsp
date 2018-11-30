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
</head>
<body style="overflow: hidden;">

<h1>datasync登录</h1>
<hr />

<form action="/login.do" method="post">
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