/**
 * 文件名：FavoriteDao
 * 作者：liuzeming
 * 时间：2019/4/23 8:54
 * 描述：
 */

package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    public Favorite findByRidAndUid(int rid,int uid);

    public int findCountByRid(int rid);

    public void add(int parseInt, int uid);
}
