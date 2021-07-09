package util.exception;

// 功能： 若某一客户端退出服务器与其失去连接，抛出这个自定义异常异常

public class UserOffLineException extends Exception {
    public UserOffLineException() {}
    public UserOffLineException(String msg) {
        super(msg);
    }
}
