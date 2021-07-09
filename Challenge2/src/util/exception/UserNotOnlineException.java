package util.exception;

// 功能：若对方不在线而发送失败，抛出这个自定义异常

public class UserNotOnlineException extends Exception {
    public UserNotOnlineException() {}
    public UserNotOnlineException(String msg) {
        super(msg);
    }
}
