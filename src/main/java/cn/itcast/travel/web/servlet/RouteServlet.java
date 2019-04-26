package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService service=new RouteServiceImpl();
    private FavoriteService favoriteService=new FavoriteServiceImpl();
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPageStr = request.getParameter("currentPage");
        String PageSizeStr = request.getParameter("PageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        //2.处理参数
        int cid=0;
         if (cidStr!=null&&cidStr.length()>0&&!"null".equals(cidStr)){
             cid=Integer.parseInt(cidStr);
         }

        int PageSize=0;
        if (PageSizeStr!=null&&PageSizeStr.length()>0){
            PageSize=Integer.parseInt(PageSizeStr);
        }else{
            PageSize=5;
        }

        int currentPage=0;
        if (currentPageStr!=null&&currentPageStr.length()>0){
            currentPage=Integer.parseInt(currentPageStr);
        }else{
            currentPage=1;
        }
        //3.调用service查询pageBean对象
           PageBean<Route> route= service.pageQuery(cid,PageSize,currentPage,rname);
       // System.out.println(route);
        //4.将pageBean对象序列化为json
          writeValue(route,response);


    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          String rid = request.getParameter("rid");
          Route route= service.findOne(Integer.parseInt(rid));

          writeValue(route,response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");

        //User user = (User)request.getAttribute("user");
        User user = (User)request.getSession().getAttribute("user");
        int uid;
        if(user==null){
              uid=0;
        }else{
            uid=user.getUid();
        }
         boolean flag= favoriteService.isFavorite(Integer.parseInt(rid),uid);

        System.out.println("RouteServletisFavorite="+user+"是否收藏过了"+flag);
        writeValue(flag,response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");

        //User user = (User)request.getAttribute("user");
        User user = (User)request.getSession().getAttribute("user");

        int uid;
        if(user==null){
            return;
        }else{
            uid=user.getUid();
        }
        System.out.println("serlletuid="+uid);
         favoriteService.addFavorite(rid,uid);
       // System.out.println(flag);
        //writeValue(flag,response);
    }

    }
