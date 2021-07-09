package cs.ManageThreadorFrame;

import cs.Client.thread.ClientConServerThread;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

// 功能：管理一台电脑等多个不同账号的多线程类

public class ManageClientConServerThread {

    // HashMap存登录的qq号码和那个线程，有且只有一份

    public static ConcurrentHashMap<String, ClientConServerThread> hashMap = new ConcurrentHashMap<>();

    // 设计一个方法将登录上的qq存入HashMap

     public static void addClientConServerThread(String qqId,ClientConServerThread c) {
         hashMap.put(qqId,c);
     }

    // 设计一个方法，通过qqId获得该线程

    public static ClientConServerThread getClientConServerThread(String qqId) {
        return hashMap.get(qqId);
    }


}
