package dao;
import domain.UserInfo;



public interface UserInfoDao {

    // 设计一个方法  根据account查询用户资料卡信息

    public UserInfo findByAccount(String account);

    // 设计一个方法  保存用户的资料卡信息

    public void save(UserInfo userInfo);
}
