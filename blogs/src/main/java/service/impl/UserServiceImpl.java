package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.User;
import service.UserService;

import java.util.HashMap;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    // 设计一个方法  注册用户

    @Override
    public boolean register(User user) {
        // 1.根据account查询用户
        User u = userDao.findByAccount(user.getAccount());
        if (u != null) {
            // 账号存在，注册失败
            return false;
        }
        // 账号不存在
        // 2. 保存用户信息
        userDao.save(user);
        return true;

    }

    // 设计一个方法   登录

    @Override
    public User login(User user) {
        return userDao.findByAccountAndPassword(user.getAccount(), user.getPassword());
    }
}
