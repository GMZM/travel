/**
 * 文件名：BaseServlet
 * 作者：liuzeming
 * 时间：2019/4/2 16:29
 * 描述：
 */

package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         //完成请求路径
         //1.获取请求路径
        String uri=req.getRequestURI();
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        //System.out.println("方法名称"+methodName);//方法名称find
        //3.获取方法对象
        //谁调用我，我代表是谁
        //System.out.println(this);
        try {
            Method method= this.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
             //4.执行方法
            //method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //直接将传入的对象序列化为json，并写回给客户端
    public void writeValue(Object obj,HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
            mapper.writeValue(resp.getOutputStream(),obj);
    }

     //将传入的对象序列化为json，返回给调用者
    public String writeValuyeAsString(Object object ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return   mapper.writeValueAsString(object);
    }

}
