package util.exception;

// 功能：若取消文件或图片的发送，抛出这个自定义异常

public class CancelSendException extends Exception {
    public CancelSendException() {}
    public CancelSendException(String msg) { super(msg); }
}
