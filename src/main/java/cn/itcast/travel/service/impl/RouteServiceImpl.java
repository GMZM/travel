/**
 * 文件名：RouteServiceImpl
 * 作者：liuzeming
 * 时间：2019/4/15 15:30
 * 描述：
 */

package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();

    private RouteImgDao routeImgDao=new RouteImgDaoImpl();

    private SellerDao sellerDao=new SellerDaoImpl();

    private FavoriteDao favoriteDao=new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int pageSize, int currentPage,String rname) {
        PageBean<Route> pb = new PageBean<Route>();

        pb.setPageSize(pageSize);
        //System.out.println(pageSize);

        pb.setCurrentPage(currentPage);
        int totalCount = routeDao.findTatolCount(cid,rname);

        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start=(currentPage-1)*pageSize;//开始的纪录数
        List<Route> list=routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        //设置总页数=总记录数/每页显示的条数
        int totalPage=totalCount%pageSize== 0 ? totalCount/pageSize:(totalCount/pageSize)+1;
        pb.setTotalPage(totalPage);
        System.out.println(totalPage);
        return pb;
    }

    @Override
    public Route findOne(int rid) {
       Route route=  routeDao.findOne(rid);
       //图片的集合
        List<RouteImg> routeImgList = routeImgDao.findByRid(rid);
        route.setRouteImgList(routeImgList);
        //商家的信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //多少次的收藏
         int count= favoriteDao.findCountByRid(rid);
         route.setCount(count);
        return route;
    }


}
