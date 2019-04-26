/**
 * 文件名：UserServlet
 * 作者：liuzeming
 * 时间：2019/4/2 16:29
 * 描述：
 */

package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {


    private UserService userService = new UserServiceImpl();


    //注册
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //验证校验
        String check=req.getParameter("check");
        //从Session中获取验证码
        HttpSession session = req.getSession();
        String string = (String) session.getAttribute( "CHECKCODE_SERVER");
        //为了保证验证码只能使用一次
        session.removeAttribute(string);
        //比较
        if( string==null||!string.equalsIgnoreCase(check)){
            System.out.println("验证码");
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            String s = new ObjectMapper().writeValueAsString(info);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(s);
            return;
        }

        /* 1.获取数据 */
        Map<String, String[]> map = req.getParameterMap();
        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //3.调用service来完成注册
        //UserService userService = new UserServiceImpl();
        boolean flag= userService.regist(user);
        //4.响应结果
        ResultInfo info = new ResultInfo();
        if (flag){
            info.setFlag(true);
        }else {
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }

        //5.将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        //6.将json数据写回客户端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);

    }

    //登陆
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String check=req.getParameter("check");
        //从Session中获取验证码
        HttpSession session = req.getSession();
        String string = (String) session.getAttribute( "CHECKCODE_SERVER");
        //为了保证验证码只能使用一次
        session.removeAttribute(string);
        //比较
        if( string==null||!string.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            String s = new ObjectMapper().writeValueAsString(info);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(s);
            return;
        }

        User user=new User();
        //1.获得参数
        Map<String, String[]> map = req.getParameterMap();
        //2.封装对象

        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service来完成登陆
       // UserService service=new UserServiceImpl();
        User u=userService.login(user);
        ResultInfo info = new ResultInfo();
        //4.判断是否为null
        if (u==null){
            //用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //5.判断邮箱是否激活
        if (u!=null&& u.getStatus().equals("N")){
            info.setFlag(false);
            info.setErrorMsg("邮箱未激活");
        }
        //6.登陆成功
        if (u!=null&& u.getStatus().equals("Y")){
            info.setFlag(true);
        }
        session.setAttribute("user",u);
        //7.将info对象序列化为json对象
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //8.将json对象写回客户端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);

       /* //7.将info对象序列化为json对象
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        //8.将json对象写回客户端
         mapper.writeValue(resp.getOutputStream(),info);
*/
    }

    //激活邮箱
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取激活码
        String code = req.getParameter("code");
        if (code != null) {
           // UserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);
            String msg = null;
            if (flag) {
                msg = "激活成功，请<a href='http://localhost:8080/travel/login.html'>登陆</a>";
            } else {
                msg = "激活未成功,请联系管理员";
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(msg);

        }
    }
     //查找一个用户
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从Session中获取登录用户
        Object user = req.getSession().getAttribute("user");
       // System.out.println(user);
        //将user写回客户端
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        mapper.writeValue(resp.getOutputStream(),user);
    }
     //退出
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.销毁Session
        req.getSession().invalidate();
        //2.跳转页面
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }
}
