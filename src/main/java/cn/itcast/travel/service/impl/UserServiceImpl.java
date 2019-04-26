/**
 * 文件名：UserServiceImpl
 * 作者：liuzeming
 * 时间：2019/3/30 9:49
 * 描述：
 */

package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    public boolean regist(User user) {
        User u = userDao.findByUserName(user.getUsername());
        if (u != null) {
            return false;
        }
        //1.设置激活码 ，唯一的字符串
         user.setCode(UuidUtil.getUuid());
        //2.设置激活状态
        user.setStatus("N");
        userDao.save(user);
        //3.激活邮箱发送，邮箱正文
        String content="<a href='http://localhost:8080/travel/user/active?code="+user.getCode()+"'> 点击激活</a>";

        MailUtils.sendMail(user.getEmail(),content,"激活邮件");


        return true;
    }

    @Override
    //激活用户
    public boolean active(String code) {
        //1根据激活码，查询用户对象
        User  user=userDao.findByCode(code);
        if (user!=null){
        //2.调用dao的方法修改激活状态
            userDao.updateStatus(user);
            return true;
        }else{
           return false;
        }


    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
