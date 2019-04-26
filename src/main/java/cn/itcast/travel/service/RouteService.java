/**
 * 文件名：RouteService
 * 作者：liuzeming
 * 时间：2019/4/15 15:29
 * 描述：
 */

package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    public PageBean<Route> pageQuery(int cid, int pageSize, int currentPage,String rname);

    public Route findOne(int rid);


}
