package service;

import domain.User;

public interface UserService {

    // 设计一个方法  注册用户

    boolean register(User user);

    // 设计一个方法  登录用户

    User login(User user);
}
