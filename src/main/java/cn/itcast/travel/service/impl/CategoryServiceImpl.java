/**
 * 文件名：CategoryServiceImpl
 * 作者：liuzeming
 * 时间：2019/4/4 11:39
 * 描述：
 */

package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1获取Jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2可用sortset来排序
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3查询Sortedset中的分数（cid）和值（cname）
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        //2.判断集合是否为空
        List<Category> all = null;
        if (categorys == null || categorys.size() == 0) {
           // System.out.println("从数据库查询");
            //3为空，需要从数据库查询,将数据存入到redis中
            //3.1从数据库中查询
            all = categoryDao.findAll();
            //3.2将数据存储到redis中
            for (int i = 0; i < all.size(); i++) {
                jedis.zadd("category", all.get(i).getCid(), all.get(i).getCname());
            }

        } else {
           // System.out.println("从redis中查询");
            //4.如果不为空，将set数据存储到list中
            all = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                all.add(category);
            }

        }
        return all;

    }

}
