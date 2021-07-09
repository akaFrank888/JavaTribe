package dao;
import domain.User;




public interface UserDao {

    // 设计一个方法  根据account查询用户

    public User findByAccount(String account);

    // 设计一个方法  保存用户注册信息

    public void save(User user);

    // 设计一个方法   根据account和password查询用户

    public User findByAccountAndPassword(String account, String password);
}
