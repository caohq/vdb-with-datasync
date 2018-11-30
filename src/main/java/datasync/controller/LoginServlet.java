package datasync.controller;

import datasync.service.LoginService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class LoginServlet extends HttpServlet {
    private LoginService loginService = new LoginService();

    protected void service(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        String loginNotice = "";
        int loginStatus = 0; // log success or not， 0 ：success, 1: failed, notice : username or password is wrong
        loginStatus = loginService.validateLogin(userName, password);

        String retView = "";
        if (loginStatus == 1)
        {
            retView = "redirect:/starter.jsp";
        }
        else
        {
            retView = "redirect:/index.jsp";
        }

        res.sendRedirect(retView);
    }
}