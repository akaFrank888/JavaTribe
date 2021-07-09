package cs.ManageThreadorFrame;

import cs.Server.thread.SerConClientThread;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

// 功能：该类为了管理服务器与客户端交互的线程

public class ManageSerConClientThread {

    // 创建一个HashMap(static)，便于管理登录的客户端. 但value是线程不是user，优点在于还可以用于看用户是否在线

    public static ConcurrentHashMap<String,SerConClientThread> hashMap = new ConcurrentHashMap<>();



    // 设计一个方法 登录成功就向hashMap中添加一个客户端通讯线程

    public static void addSerConClientThread(String clientName, SerConClientThread thread) {
        hashMap.put(clientName, thread);
    }

    // 设计一个方法 通过clientName获取线程

    public static SerConClientThread getSerConClientThread(String clientName) {
        return hashMap.get(clientName);
    }

    // 设计一个方法 返回当前在线的人的情况

    public static String getAllOnLineUserId() {
        // 只有在这里才能取到所有在线的人 id-thread
        // 所以要在这里得到之后传出去


        // HashMap的遍历---iterator
        Iterator it= hashMap.keySet().iterator();
        String  res = "";

        System.out.println("getOnline里的hashMap的size是"+hashMap.size());
        while(it.hasNext())
        {
            res+=it.next().toString()+" ";
        }
        return res;
    }

/*
        下面这两个方法通通取不到

        public static SerConClientThread[] getAllOnlineThread() {
        int i=0;
        SerConClientThread[] threads = new SerConClientThread[hashMap.size()];
        Iterator it= hashMap.keySet().iterator();

        while(it.hasNext())
        {
            String key = (String)it.next();
            SerConClientThread thread = hashMap.get(key);
            threads[i++] = thread;
        }
        System.out.println("新集合的hash的size==" + hashMap.size());
        System.out.println("新集合的size==" + threads.length);
        return threads;
    }

    public static GetMap getHappy() {
        // 所以要在这里得到之后传出去
        GetMap getMap = new GetMap(hashMap);
        System.out.println("getHappy里hashMap的size是"+hashMap.size());
        return getMap;
    }*/
}
