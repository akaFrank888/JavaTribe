package cs.Client.domain;

import javax.swing.*;
import java.io.Serializable;



// 功能： 交互时，用来发送与接收消息的对象

public class Message_Client implements Serializable {

    // mesType 代表信息类型---一共分为8种，其通过接口的方式为其设定（MessageType）
    // message存普通消息的内容，icon存图片，path存文件路径/图片路径
    // message对于普通消息存内容，对于文件/图片存类型（统一为 txt 或 img）

    private String mesType;

    private String sender;
    private String receiver;

    private String date;
    private String message;

    private Icon icon;
    private String path;

    // 是否在线与是否已经为好友的标记

    private boolean ifOnline;
    private boolean ifFriend;

    public Message_Client(){}
    public Message_Client(String mesType, String sender, String receiver, String date, String message, Icon icon, String path,boolean ifOnline,boolean ifFriend) {
        this.mesType = mesType;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.message = message;
        this.icon = icon;
        this.path = path;
        this.ifOnline = ifOnline;
        this.ifFriend = ifFriend;
    }


    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
    public String getMesType() {
        return mesType;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    public String getSender(){
        return this.sender;
    }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public Icon getIcon() {
        return icon;
    }
    public void setIcon(Icon icon) {
        this.icon = icon;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public boolean getIfOnline() {
        return ifOnline;
    }
    public void setIfOnline(boolean ifOnline) {
        this.ifOnline = ifOnline;
    }
    public boolean getIfFriend() {
        return ifFriend;
    }
    public void setIfFriend(boolean ifFriend) {
        this.ifFriend = ifFriend;
    }
}
