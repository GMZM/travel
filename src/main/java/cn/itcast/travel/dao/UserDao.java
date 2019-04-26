package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    //根据用户名查找
    public User findByUserName(String username);

    //添加用户信息
    public void save(User user);

    User findByCode(String code);

    void updateStatus(User user);


    User findByUsernameAndPassword(String username, String password);
}
