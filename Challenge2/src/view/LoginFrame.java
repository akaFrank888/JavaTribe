package view;

import cs.Client.domain.MessageType;
import cs.Client.domain.Message_Client;
import cs.ManageThreadorFrame.ManageClientConServerThread;
import cs.Client.domain.User_Client;
import cs.Client.service.LogInService;
import cs.ManageThreadorFrame.ManageUserFrame;
import util.DataBaseCon.DataBaseConList;
import util.DataBaseCon.InitDataBase;
import util.others.AllKeyListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

@SuppressWarnings("all")

public class LoginFrame extends BaseFrame {

    // 规定并设计多个Frame的统一构造方法，所以用到了 init()方法
    public LoginFrame(){
        this.init();
    }
    public LoginFrame(String title){
        // super要写在this上面
        super(title);
        this.init();
    }

    // 创建一个面板
    private JPanel mainPanel = new JPanel();
    // 创建title显示标题
    private JLabel titleLabel = new JLabel("爪哇部落聊天室");
    // 创建账号和密码的标题
    private JLabel accountLabel = new JLabel("账 户：");
    private JLabel passwordLabel = new JLabel("密 码：");
    // 创建输入账号和密码的文本框/密码框
    private JTextField accountField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    // 创建三个按钮
    private JButton loginButton = new JButton("登 录");
    private JButton registerButton = new JButton("注 册");


    @Override
    protected void setFontAndSoOn() {
        // 设置panel的布局管理为自定义方式
        mainPanel.setLayout(null);
        // 设置标题的位置和字体大小
        titleLabel.setBounds(150,40,340,35);
        titleLabel.setFont(new Font("黑体",Font.BOLD,34));
        // 设置用户名label位置和字体大小
        accountLabel.setBounds(94,124,90,30);
        accountLabel.setFont(new Font("黑体",Font.BOLD,24));
        // 设置用户名filed位置和字体大小
        accountField.setBounds(204,124,260,30);
        accountField.setFont(new Font("黑体",Font.BOLD,24));
        // 设置密码label位置和字体大小
        passwordLabel.setBounds(94,174,90,30);
        passwordLabel.setFont(new Font("黑体",Font.BOLD,24));
        // 设置密码field位置和字体大小
        passwordField.setBounds(204,174,260,30);
        passwordField.setFont(new Font("黑体",Font.BOLD,24));
        passwordField.setEchoChar('*');
        //设置登录Button的位置和文字大小
        loginButton.setBounds(40,232,130,30);
        loginButton.setFont(new Font("黑体",Font.BOLD,22));
        // 设置注册Button的位置和文字大小
        registerButton.setBounds(380,232,130,30);
        registerButton.setFont(new Font("黑体",Font.BOLD,22));
    }

    // 将所有的组件添加至窗体上
    @Override
    protected void addElement() {
        mainPanel.add(titleLabel);
        mainPanel.add(accountLabel);
        mainPanel.add(accountField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);
        this.add(mainPanel);
    }

    // 绑定事件监听器
    @Override
    protected void addListener() {
        // ActionListener，MouseListener，KeyListener中，选择了ActionListner（因为只有一个方法）
        // 1.监听登录按钮
        ActionListener listener = new ActionListener() {

            // 用匿名内部类实现ActionListener接口  目的是能用到组件
            @Override
            public void actionPerformed(ActionEvent e) {

                // 1.获取用户输入的账号和密码
                // 从登录窗口上的组件内获取   文本框  密码框
                String account = accountField.getText();
                // 密码框类型是char[]，所以char[] ---> String
                String password = new String(passwordField.getPassword());

                // 2.调用之前Service层的登录方法
                // 创建服务层的一个对象 和 NewUser对象
                LogInService service = new LogInService();
                User_Client newUser = new User_Client(account,password);
                // 在view层里调用service层的方法
                // 而调用checkUser后，就已经打通了user和server的线程了
                String result = service.checkUser(newUser);
                System.out.println(newUser.getAccount()+result);
                result = result.trim();

                // 3.判定最终的结果
                if(result.equals("登录成功")) {

                    try {

                        // a.先在库中建一个该用户的list_表和chat_表，并把自己写进数据库中他的list_表中
                        String listTablleName = "list_"+newUser.getAccount();
                        String chatTableName = "chat_"+newUser.getAccount();
                        // 该方法内部会判断是否存在两个表
                        InitDataBase.createEveryUser_ListAndChatTable(listTablleName,chatTableName);

                        if (!DataBaseConList.getFriends(account).contains(account)) {
                            boolean b = DataBaseConList.addFriend(account,account);
                        }else {
                            System.out.println("该用户已经在数据库的表中");
                        }

                        // b.关闭登录页面，弹出新的用户界面，并把这个UserFrame加入到HashMap中
                        LoginFrame.this.dispose();
                        UserFrame userFrame = new UserFrame(account + "#用户界面");
                        ManageUserFrame.addQqFriendList(newUser.getAccount(), userFrame);

                        // c.包一个 Message  指明我要的是这个 qq 号的好友情况.
                        Message_Client m = new Message_Client();
                        m.setMesType(MessageType.message_get_onLineFriend);
                        m.setSender(newUser.getAccount());

                        // d.在现实好友列表界面之后，请求得到在线好友的包
                        // 因为线程已经打通所以可以通过那个管理线程的类来得到socket
                        ObjectOutputStream oos = new ObjectOutputStream
                                (ManageClientConServerThread.getClientConServerThread(newUser.getAccount()).getSocket().getOutputStream());
                        oos.writeObject(m);
                        // 至于接收服务器返回的包，要在ClientConServerThread接收

                    }
                    catch (ClassNotFoundException | SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                        // 弹出一个警告框 告诉登录失败啦
                        JOptionPane.showMessageDialog(LoginFrame.this,result);
                        // 设置文本框和密码框的值为空
                        accountField.setText("");
                        passwordField.setText("");
                }
            }
        };
        loginButton.addActionListener(listener);// 观察者模式  listener观察loginButton


        // 2.监听注册按钮
        ActionListener anotherListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 将登录窗口隐藏
                LoginFrame.this.setVisible(false);
                // 弹出新的注册界面
                new RegisterFrame("注册界面");
            }
        };
        registerButton.addActionListener(anotherListener);// 观察者模式

        // 3.监听回车快捷键
        AllKeyListener keyListener = new AllKeyListener(loginButton);
        accountField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);
    }

    @Override
    protected void setFrameSelf() {
        // 设置窗体起始位置和大小
        this.setBounds(600,280,560,340);
        // 设置点击关闭退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体大小不可拖拽
        this.setResizable(false);
        // 设置窗体显示状态
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }

}
