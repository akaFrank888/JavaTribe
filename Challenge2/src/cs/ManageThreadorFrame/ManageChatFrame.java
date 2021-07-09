package cs.ManageThreadorFrame;

import view.ChatFrame;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

// 功能：这是一个管理用户聊天界面的类

public class ManageChatFrame {

    // String是sender和receiver的聊天界面（是唯一的，所以点第二次也不会再重复出现聊天界面）

    private static ConcurrentHashMap<String, ChatFrame> hashMap = new ConcurrentHashMap<>();

    // 方法：添加
    public static void addQqChat(String loginIdAndFriendId, ChatFrame qqchat) {
        hashMap.put(loginIdAndFriendId, qqchat);
    }

    // 方法：取出
    public static ChatFrame getQqChat(String logInAndFriendId) {
        return hashMap.get(logInAndFriendId);
    }
}
