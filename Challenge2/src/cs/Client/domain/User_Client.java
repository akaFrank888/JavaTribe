package cs.Client.domain;

import java.io.Serializable;

// 功能 : 交互时，用来注册与登录的对象

public class User_Client implements Serializable {

    //属性来存文件中的账号密码

    private String account;
    private String password;

    public User_Client(){}
    public User_Client(String account, String password){
        this.account = account;
        this.password = password;
    }

    public String getAccount(){
        return this.account;
    }
    public void setAccount(String account){
        this.account = account;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
