package cs.Server.thread;

import cs.Server.myServer.MyServer;



// 功能： 实现启动服务器的时候，关闭服务器按钮不失效

public class StartServerThread extends Thread {

    @Override
    public void run() {
        // 开启服务器
        new MyServer();
    }
}
