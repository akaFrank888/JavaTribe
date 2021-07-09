package cs.Client.domain;

import java.io.Serializable;
import java.sql.Blob;

// 功能：交互时，用来显示历史记录的对象

public class Message_Record implements Serializable,Comparable<Message_Record> {


    private String sender;
    private String receiver;
    private String message;
    private Blob imgOrFile;
    private String date;
    private String downloadPath;


    public Message_Record() {}
    public Message_Record(String sender, String receiver, String message, Blob imgOrFile, String date,String downloadPath) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.imgOrFile = imgOrFile;
        this.date = date;
        this.downloadPath = downloadPath;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Blob getImgOrFile() {
        return imgOrFile;
    }

    public String getDate() {
        return date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImgOrFile(Blob imgOrFile) {
        this.imgOrFile = imgOrFile;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public String toString() {
        return "sender:"+this.sender+", receiver:"+this.receiver+", date:"+this.date;
    }

    @Override
    public int compareTo(Message_Record o) {
        // 因为要根据时间给message排序从而实现显示历史记录
        // 所以要将该类实现Comparable，并重写compareTo方法，使得用时间排序message

        return (this.getDate().compareTo(o.getDate()));
    }
}