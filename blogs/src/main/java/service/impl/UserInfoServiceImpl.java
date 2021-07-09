package service.impl;

import dao.UserInfoDao;
import dao.impl.UserInfoDaoImpl;
import domain.UserInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import service.UserInfoService;
import util.JedisUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class UserInfoServiceImpl implements UserInfoService {

    private UserInfoDao dao = new UserInfoDaoImpl();

    @Override
    public boolean fillIn(UserInfo userInfo) {
        // 如果有一项信息未填就保存失败
        if (userInfo.getGender() == null || "".equals(userInfo.getAge()+"")|| userInfo.getStuID() == null || userInfo.getMaster() == null || userInfo.getMailBox() == null) {
            return false;
        }

        dao.save(userInfo);
        return true;
    }


    // redis(冷数据)，mysql（热数据）的查询

    @Override
    public UserInfo showUserInfo(String account) {
        UserInfo userInfo = new UserInfo();
        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 查询map
        Map<String, String> map = jedis.hgetAll(account);
        // 3. 判断
        if (map == null || map.size() == 0) {
            // 4. 先从数据库中查，再将数据存入redis
            System.out.println("从数据库中查询数据并保存数据到redis中");
            userInfo = dao.findByAccount(account);
            // 存入redis
            jedis.hset(account, "name", userInfo.getName());
            jedis.hset(account, "gender", userInfo.getGender());
            jedis.hset(account, "age", userInfo.getAge() + "");
            jedis.hset(account, "stuID", userInfo.getStuID());
            jedis.hset(account, "master", userInfo.getMaster());
            jedis.hset(account, "mailBox", userInfo.getMailBox());
            jedis.hset(account, "account", userInfo.getAccount());
        } else {
            // 4.从redis中查
            Map<String, String> userInfoMap = jedis.hgetAll(account);
            System.out.println("从redis中查询");
            try {
                BeanUtils.populate(userInfo,userInfoMap);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }finally {
                JedisUtil.close(jedis);
            }
        }
        return userInfo;

    }


}
