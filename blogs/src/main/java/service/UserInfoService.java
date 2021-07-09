package service;

import domain.UserInfo;

public interface UserInfoService {

    // 设计一个方法   用户填写资料卡

    boolean fillIn(UserInfo userInfo);

    // 设计一个方法   显示资料卡       （ctrl+单机-->用处；ctrl+alt+单机-->实现类）

    UserInfo showUserInfo(String account);

}
