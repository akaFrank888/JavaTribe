package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FileOpenOrNot extends BaseFrame {

    private String path;
    private JPanel mainPanel = new JPanel();
    private JLabel label = new JLabel("是否打开接收到的文件");
    private JButton openButton = new JButton("打开");
    private JButton closeButton = new JButton("关闭");

    // 规定并设计多个Frame的统一构造方法，所以用到了 init()方法
    public FileOpenOrNot(){
        this.init();
    }
    public FileOpenOrNot(String title,String path){
        // super要写在this上面
        super(title);
        this.init();
        this.path = path;
    }
    @Override
    protected void setFontAndSoOn() {
        // 设置panel的布局管理为自定义方式
        mainPanel.setLayout(null);
        // 设置标题的位置和字体大小
        label.setBounds(70,30,160,60);
        label.setFont(new Font("黑体",Font.BOLD,15));
        //设置登录Button的位置和文字大小
        openButton.setBounds(70,120,70,40);
        openButton.setFont(new Font("黑体",Font.BOLD,15));
        // 设置注册Button的位置和文字大小
        closeButton.setBounds(160,120,70,40);
        closeButton.setFont(new Font("黑体",Font.BOLD,15));
    }

    @Override
    protected void addElement() {

        mainPanel.add(label);
        mainPanel.add(openButton);
        mainPanel.add(closeButton);
        this.add(mainPanel);

    }

    @Override
    protected void addListener() {

        // 1.监听打开按钮
        ActionListener openListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    File file = new File(path);
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        openButton.addActionListener(openListener);

        // 2.监听打开按钮
        ActionListener closeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileOpenOrNot.this.dispose();
            }
        };
        closeButton.addActionListener(closeListener);
    }
    @Override
    protected void setFrameSelf() {
        // 设置窗体起始位置和大小
        this.setBounds(600,280,300,200);
        // 设置窗体大小不可拖拽
        this.setResizable(false);
        // 设置窗体显示状态
        this.setVisible(true);
    }
}
