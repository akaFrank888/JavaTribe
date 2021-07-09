package view;


import javax.swing.*;


@SuppressWarnings("all")

// 模板模式
// 设计一个规则  任何窗口想要画出来  执行流程固定

public abstract class BaseFrame extends JFrame {
    // 继承了JFrame，就不用再建窗体了
    // 除了构造方法，其他方法都是protected

    public BaseFrame(){}
    public BaseFrame(String title){
        super(title);
    }

    // 将四个设计方法封装成一个init方法
    protected void init(){
        this.setFontAndSoOn();
        this.addElement();
        this.addListener();
        this.setFrameSelf();
    }


    // 1.布局，组件位置，字体属性，背景颜色等等
    protected abstract void setFontAndSoOn();
    // 2.将属性添加到窗体里
    protected abstract void addElement();
    // 3.添加事件监听
    protected abstract void addListener();
    // 4.设置窗体自身
    protected abstract void setFrameSelf();

}
