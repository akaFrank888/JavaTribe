package domain;

/**
 * 博客对象
 */
public class Blog {

    private String title;
    private String content;

    // 时间用String存储，而不用Date，TimeStamp等

    private String date;
    private String account;

    public Blog() {}
    public Blog(String title, String content, String date, String account) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.account = account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
