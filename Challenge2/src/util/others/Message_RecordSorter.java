package util.others;


import cs.Client.domain.Message_Record;

import java.util.ArrayList;
import java.util.Collections;

// 功能；按照时间顺序给messageBox里的message_record排序（message_record重写了compareTo的前提下）

public class Message_RecordSorter {

    private ArrayList<Message_Record> messageBox = new ArrayList<>();

    public Message_RecordSorter(ArrayList<Message_Record> messageBox) {
        this.messageBox = messageBox;
    }
    public ArrayList<Message_Record> arrayListSorter(){
        Collections.sort(messageBox);
        return messageBox;
    }

}
