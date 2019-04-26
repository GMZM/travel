/**
 * 文件名：ActiveUserServl
 * 作者：liuzeming
 * 时间：2019/4/1 9:44
 * 描述：
 */

package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServl extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      //获取激活码
        String code = req.getParameter("code");
        if(code!=null){
            UserService  userService=new UserServiceImpl();
            boolean flag =userService.active(code);
             String msg=null;
            if (flag){
                msg="激活成功，请<a href='login.html'>登陆</a>";
            }else{
                 msg="激活未成功,请联系管理员";
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(msg);

        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
