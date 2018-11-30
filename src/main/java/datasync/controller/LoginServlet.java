
<<<<<<< HEAD
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
=======
//package datasync.controller;
//
//import datasync.service.LoginService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class LoginServlet extends HttpServlet {
//    private LoginService loginService = new LoginService();
//
//    protected void service(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
//        String userName = request.getParameter("userName");
//        String password = request.getParameter("password");
//
//        String loginNotice = "";
//        int loginStatus = 0; // log success or not， 0 ：success, 1: failed, notice : username or password is wrong
//        loginStatus = loginService.validateLogin(userName, password);
//
//        String retView = "";
//        if (loginStatus == 1)
//        {
//            request.getSession().setAttribute("userName", userName);
//            retView = "redirect:/index";
//        }
//        else
//        {
//            retView = "redirect:/";
//            attributes.addFlashAttribute("loginNotice", "用户名或密码错误！");
//        }
//    }
//}
>>>>>>> ceabd25af86e5631970c8b80ec0063a7a10951df
