package cs.Client.service;

import cs.Client.myClient.MyClient;
import cs.Client.domain.User_Client;
import java.io.IOException;

// 功能：用于登录的业务逻辑

public class LogInService {

    // 设计一个方法： 用于登录的业务逻辑

    public String  checkUser(User_Client user) {
        String s = null;
        try {
            s = new MyClient().sendLogInInfoToServer(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
