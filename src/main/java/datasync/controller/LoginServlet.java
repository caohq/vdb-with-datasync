package datasync.controller;

import datasync.service.login.LoginService;
import datasync.utils.MD5;
import vdb.mydb.VdbManager;
import vdb.mydb.security.VdbSecurityManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    private LoginService loginService = new LoginService();
    private MD5 md5=new MD5();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out=response.getWriter();
        VdbSecurityManager vsm = VdbManager.getInstance().getSecurityManager();
        String userName = request.getParameter("j_username");
        if(vsm.getUser(userName)==null){//优先判断账号是否存在！
            out.println("账号不存在！");
            return;
        }
        String password= request.getParameter("j_password")+"{"+userName+"}";
        String passwordMd5=md5.getMD5(password);//将账号密码转化为vdb存储密码串，加密
        String derbyPassword=vsm.getUser(userName).getPassword();//获取vdb中存储的加密密码
        if(!derbyPassword.equals(passwordMd5)){
            out.println("密码错误！");
            return;
        }

        out.println("登录成功！");

        String password1=request.getParameter("j_password");
//        int loginStatus = 0; // log success or not， 0 ：success, 1: failed, notice : username or password is wrong
//        loginStatus = loginService.validateLogin(userName, password1);
//
//        String loginNotice = "";
//
//        if (loginStatus == 1)
//        {
//            loginNotice = "success";
//        }
//        else
//        {
//            loginNotice = "fail";
//        }

//        response.getWriter().println(loginNotice);
    }
}

