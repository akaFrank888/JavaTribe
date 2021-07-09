package util.others;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// 功能： 键盘监听器的外部类，以实现利用快捷键登录、注册、添加、删除好友，并且避免冗余

public class AllKeyListener implements KeyListener {

    private JButton button;

    public AllKeyListener(JButton button) {
        this.button = button;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            button.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
