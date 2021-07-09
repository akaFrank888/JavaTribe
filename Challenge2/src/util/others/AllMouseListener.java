package util.others;

import cs.ManageThreadorFrame.ManageChatFrame;
import view.ChatFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


// 功能： 鼠标监听器的外部类

public class AllMouseListener implements MouseListener {

    private String userName;
    private String receiverName;
    private JLabel newLabel;

    public AllMouseListener(String userName, String receiverName,JLabel newLabel) {
        this.userName = userName;
        this.receiverName = receiverName;
        this.newLabel = newLabel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ChatFrame chatFrame = new ChatFrame(userName + "正在与" + receiverName + "好友聊天");
        // 把聊天界面加入到管理类  第一个参数中再添一个空格，以后好处理
        ManageChatFrame.addQqChat(userName+" "+receiverName,chatFrame);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        newLabel.setForeground(Color.BLUE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        newLabel.setForeground(Color.BLACK);
    }
}
