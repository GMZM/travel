/**
 * 文件名：RegistUserServlet
 * 作者：liuzeming
 * 时间：2019/3/30 9:40
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
import java.util.Map;

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        UserService userService = new UserServiceImpl();
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
