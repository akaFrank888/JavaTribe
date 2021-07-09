package view;

import cs.Client.domain.User_Client;
import util.others.AllKeyListener;
import util.DataBaseCon.DataBaseConUser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("all")

public class RegisterFrame extends BaseFrame {

    public RegisterFrame(){
        this.init();
    }
    public RegisterFrame(String title){
        super(title);
        this.init();
    }

    // 创建面板
    private JPanel mainPanel = new JPanel();
    // 创建标题
    private JLabel titleLabel = new JLabel("新用户注册");
    // 创建账号和密码的标题
    private JLabel accountLabel = new JLabel("账户：");
    private JLabel passwordLabel = new JLabel("密码：");
    // 创建输入账号和密码的文本框/密码框
    private JTextField accountField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    // 创建按钮
    private JButton registerButton = new JButton("注 册");

    @Override
    protected void setFontAndSoOn() {
        // 设置组件的位置(大小 字体 布局等等)----布局管理
        // 边界式BorderLayout(JFrame) 流式FlowLayout(JPanel)
        // 网格式GridLayout 自定义(null)
        // 设置panel的布局管理为自定义方式
        mainPanel.setLayout(null);
        // mainPanel.setBackground(Color.WHITE);
        // 设置标题组件的位置和字体大小
        titleLabel.setBounds(200,40,340,35);
        titleLabel.setFont(new Font("黑体",Font.BOLD,34));
        // 设置用户名label位置和字体大小
        accountLabel.setBounds(54,124,90,30);
        accountLabel.setFont(new Font("黑体",Font.BOLD,24));
        // 设置用户名filed位置和字体大小
        accountField.setBounds(204,124,260,30);
        accountField.setFont(new Font("黑体",Font.BOLD,24));
        // 设置密码label位置和字体大小
        passwordLabel.setBounds(54,174,90,30);
        passwordLabel.setFont(new Font("黑体",Font.BOLD,24));
        // 设置密码field位置和字体大小
        passwordField.setBounds(204,174,260,30);
        passwordField.setFont(new Font("黑体",Font.BOLD,24));
        passwordField.setEchoChar('*');
        // 设置注册按钮的位置和文字大小
        registerButton.setBounds(300,232,100,30);
        registerButton.setFont(new Font("黑体",Font.BOLD,22));
    }

    @Override
    protected void addElement() {
        // 将所有的组件添加至窗体上
        mainPanel.add(titleLabel);
        mainPanel.add(accountLabel);
        mainPanel.add(accountField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(registerButton);
        this.add(mainPanel);
    }


    // 注册不能在客户端上实现啊啊啊啊，需要和登录一样，在服务器上实现！！
    @Override
    protected void addListener() {
        // 1.监听注册按钮
        ActionListener registerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取用户输入的账号和密码
                // 从登录窗口上的组件内获取   文本框  密码框
                String account = accountField.getText();
                // 输入处不能为空
                if (accountField.getText().equals("")||passwordField.getPassword().equals("")) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "账号密码不能为空");
                }else{
                    // 1.检查用户是否存在
                    if (DataBaseConUser.getUser(account)==null) {
                        String password = new String(passwordField.getPassword());
                        String message = account + "-" + password;
                        // 2.把新账户和新密码写入缓存集合和数据库中的user_info表
                        DataBaseConUser.addUser(account, new User_Client(account, password));
                        DataBaseConUser.saveInfo(account, password);
                        // 3.关闭页面，并返回登录页面
                        RegisterFrame.this.dispose();
                        LoginFrame newLoginFrame = new LoginFrame("登录界面");
                        JOptionPane.showMessageDialog(RegisterFrame.this,"建议您关闭服务器重新执行否则可能无法登录...");
                    }else{
                        // 若用户存在
                        // 弹出一个警告框 告诉登录失败啦
                        JOptionPane.showMessageDialog(RegisterFrame.this,"用户已存在");
                        // 设置文本框和密码框的值为空
                        accountField.setText("");
                        passwordField.setText("");
                    }
                }
            }
        };
        registerButton.addActionListener(registerListener);// 观察者模式

        // 2.监听回车快捷键
        AllKeyListener keyListener = new AllKeyListener(registerButton);
        passwordField.addKeyListener(keyListener);

    }

    @Override
    protected void setFrameSelf() {
        // 设置窗体起始位置和大小
        this.setBounds(600,280,560,340);
        // 设置窗体大小不可拖拽
        this.setResizable(false);
        // 设置窗体显示状态
        this.setVisible(true);
    }
}
