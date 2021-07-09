package cs.Client.myClient;

import cs.Client.domain.User_Client;
import cs.Client.thread.ClientConServerThread;
import cs.ManageThreadorFrame.ManageClientConServerThread;

import java.io.*;
import java.net.Socket;

// 功能：客户端

public class MyClient {

    private Socket socket;


    // 设计唯一且重要的一个方法 -- 客户端发送第一次登录请求（User_Client）并返回结果（String）

    public String sendLogInInfoToServer(User_Client newUser) throws IOException {

        // 创建对象流 , 写给服务器
        socket = new Socket("127.0.0.1", 9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(newUser);


        // （期间经过了服务器核对信息，判断是否登录成功之后，传来了结果）


        // 客户端把结果读进来
        InputStream is = socket.getInputStream();
        byte[] b = new byte[20];
        is.read(b);
        String result = new String(b);
        // 因为“小推车”是数组，可能会有空格，所以用个trim
        if (result.trim().equals("登录成功")) {
            // 如果登录成功
            // 就创建并启动一个一台电脑登录多个不同账号的线程，再把它丢入HashMap中
            ClientConServerThread c = new ClientConServerThread(socket);
            c.start();
            ManageClientConServerThread.addClientConServerThread(newUser.getAccount(), c);
        }
        return result;
    }
}
