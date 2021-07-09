package cs.ManageThreadorFrame;

import view.UserFrame;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

// 功能：管理好友列表界面

// 缺憾：不同线程取这一个HashMap是不同的！！！！

public class ManageUserFrame {


    private static ConcurrentHashMap<String, UserFrame> hashMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, UserFrame> getHashMap() {
        return hashMap;
    }

    public static void addQqFriendList(String qqId, UserFrame userFrame) {
        hashMap.put(qqId, userFrame);
    }

    public static UserFrame getQqFriendList(String qqId) {
        return hashMap.get(qqId);
    }

}
