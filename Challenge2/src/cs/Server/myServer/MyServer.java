package cs.Server.myServer;

import cs.Client.domain.User_Client;
import cs.Server.thread.SerConClientThread;
import cs.ManageThreadorFrame.ManageSerConClientThread;
import util.DataBaseCon.DataBaseConUser;
import util.DataBaseCon.InitDataBase;

import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// 功能：服务器

public class MyServer {


    // 把监听代码放在构造方法中而不是主函数或者方法中，使得创建实例便可监听

    public MyServer(){
        try {
            // 先判断库，表是否存在并决定是否创建（都封装在方法里）
            InitDataBase.creatDataBase();
            InitDataBase.createUserTable();

            // 监听9999
            ServerSocket server = new ServerSocket(9999);
            System.out.println("服务器正在9999端口监听");

            // while循环的意义：如果第一次登录失败（但socket已经建成），第二次再登录时无法重新建立连接。
            // 也就是实现多个客户端连接
            while (true) {
                // 阻塞等待连接,得到客户端的socket后，开始交互
                Socket socket = server.accept();

                // 把user_client从客户端那读出来
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User_Client user = (User_Client) ois.readObject();

                // 得到user之后，就要进行验证（之后用数据库）并返回String结果

                // 登录验证
                User_Client realUser = DataBaseConUser.getUser(user.getAccount());

                OutputStream os = socket.getOutputStream();
                String s1 = "登录成功";
                String s2 = "登录失败";
                String s3 = "该用户已登录";
                if (realUser != null) {
                    if (user.getPassword().equals(realUser.getPassword())) {
                        String onlineUsers = ManageSerConClientThread.getAllOnLineUserId();
                        if (!onlineUsers.contains(user.getAccount())) {
                            os.write(s1.getBytes());
                            System.out.println(user.getAccount() + " 用户登录成功，密码为 " + user.getPassword());

                            // 二.登录成功后就开多线程 (一个qq号开一个线程)
                            // 这里的socket是各不相同的
                            SerConClientThread thread = new SerConClientThread(socket);
                            thread.beOnline = true;
                            // 启动与该客户端通信的线程，并把新线程加到HashMap中
                            ManageSerConClientThread.addSerConClientThread(user.getAccount(), thread);
                            thread.start();
                            // 三.并通知其他用户又上线一个
                            thread.notifyOtherOn(user.getAccount());
                        } else {
                            os.write(s3.getBytes());
                            socket.close();
                        }
                    } else {
                        // 传输结果(String) ， 与其同时客户端接收
                        os.write(s2.getBytes());
                        // 如果登陆失败，就关闭这个socket，等待下一次重新连接
                        socket.close();
                    }
                }else{
                    // 结果写出去(String)
                    os.write(s2.getBytes());
                    socket.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
