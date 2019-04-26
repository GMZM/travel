/**
 * 文件名：UserDaoImpl
 * 作者：liuzeming
 * 时间：2019/3/30 9:53
 * 描述：
 */

package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUserName(String username) {
        //1.定义sql
        User user = null;
        try {
            String sql = " select * from tab_user where username=? ";
            //2.执行sql 有可能报错 返回一个空
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {

        }

        return user;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
         User user=null;
        try {
            String sql="select * from tab_user where username=? and password=? ";
           // System.out.println(username);
           //System.out.println(password);
            user=template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,Code,status) " +
                " values(?,?,?,?,?,?,?,?,?)";
        template.update(sql, user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getCode(),
                user.getStatus());
    }


    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code=?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql = " update tab_user set status='Y' where uid=? ";
        template.update(sql, user.getUid());
    }
}
