package cs.Client.domain;

@SuppressWarnings("all")

// 功能：规定消息类型

// 这个接口可以被枚举代替！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！

public interface MessageType {

    // setMesType的时候直接调这些静态量

    String message_succeed="1";//表明是登陆成功
    String message_login_fail="2";//表明登录失败
    String message_comm_mes="3";//普通信息包
    String message_get_onLineFriend="4";//要求在线好友的包
    String message_ret_onLineFriend="5";//返回在线好友的包
    String message_ret_offLineFriend="6";//返回下线好友的包
    String message_ret_addFriend="7";//返回添加好友的包
    String message_ret_deleteFriend="8";//返回删除好友的包
}
