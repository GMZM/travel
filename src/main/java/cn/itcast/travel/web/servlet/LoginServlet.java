/**
 * 文件名：LoginServlet
 * 作者：liuzeming
 * 时间：2019/4/1 15:17
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
        UserService service=new UserServiceImpl();
          User u=service.login(user);
        ResultInfo info = new ResultInfo();
        //4.判断是否为null
        if (u==null){
            //用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
       /* //5.判断邮箱是否激活
        if (u!=null&& u.getStatus().equals("N")){
            info.setFlag(false);
            info.setErrorMsg("邮箱未激活");
        }*/
        //6.登陆成功
        if (u!=null&& u.getStatus().equals("N")){
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
}
