package domain;
import java.sql.Blob;


/**
 *   填写用户资料卡时交互的对象
 */

public class UserInfo {

    private String name;
    private String gender;
    private int age;
    private String stuID;
    private String master;
    private String mailBox;
    private Blob headImg;

    private String account;

    public UserInfo() {}

    public UserInfo(String name, String gender, int age, String stuID, String master, String mailBox, Blob headImg, String account) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.stuID = stuID;
        this.master = master;
        this.mailBox = mailBox;
        this.headImg = headImg;
        this.account = account;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getMailBox() {
        return mailBox;
    }

    public void setMailBox(String mailBox) {
        this.mailBox = mailBox;
    }

    public Blob getHeadImg() {
        return headImg;
    }

    public void setHeadImg(Blob headImg) {
        this.headImg = headImg;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", stuID='" + stuID + '\'' +
                ", master='" + master + '\'' +
                ", mailBox='" + mailBox + '\'' +
                ", headImg=" + headImg +
                ", account='" + account + '\'' +
                '}';
    }
}