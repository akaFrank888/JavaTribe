package view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("all")
public class RecordFrame extends BaseFrame {

    public RecordFrame() {
        this.init();
    }
    public RecordFrame(String title) {
        super(title);
        this.init();
    }

    //一个主面版+聊天记录面板按钮副面板
    private JPanel recordPanel = new JPanel();
    //创建一个聊天记录文本域
    private static JTextPane recordArea = new JTextPane();//聊天文本域
    private JScrollPane scrollPane = new JScrollPane(recordArea);//滚动条

    public static JTextPane getRecordArea() {
        return recordArea;
    }
    @Override
    protected void setFontAndSoOn() {
        //设置Panel布局管理--自定义
        recordPanel.setLayout(null);
        //设置Panel背景颜色
        recordPanel.setBackground(Color.gray);
        //设置Panel位置
        recordPanel.setBounds(0, 0, 500, 600);
        //1.聊天记录的位置 字体
        scrollPane.setBounds(0, 0, 500, 600);
        recordArea.setFont(new Font("黑体", Font.BOLD, 15));
        recordArea.setForeground(Color.cyan);
        recordArea.setEnabled(false);//文本域中的文字不能编辑
    }

    @Override
    protected void addElement() {
        recordPanel.add(scrollPane);
        this.add(recordPanel);
    }

    @Override
    protected void addListener() {
    }

    @Override
    protected void setFrameSelf() {
        //设置窗体起始位置和大小
        this.setBounds(600, 300, 500, 600);
        //设置窗体大小不可拖拽
        this.setResizable(false);
        //设置窗体显示状态
        this.setVisible(true);
    }
}
