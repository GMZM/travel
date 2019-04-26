/**
 * 文件名：FavoriteServiceImpl
 * 作者：liuzeming
 * 时间：2019/4/23 9:05
 * 描述：
 */

package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao=new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(int rid,int uid) {

        Favorite favorite = favoriteDao.findByRidAndUid(rid,uid);
        return favorite!=null;
    }

    @Override
    public void addFavorite(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }
}
