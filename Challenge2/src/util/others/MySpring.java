package util.others;

import java.util.HashMap;
@SuppressWarnings("all")

//目的是为了管理对象的产生
//对象的控制权交给当前来负责  IOC 对象控制反转（就是以后不是我自己new，是调MySpring）
//生命周期托管的方式实现了对象的单例 （反射）
// 功能：管理对象     因为考核不让用架构，所以没用这个类


public class MySpring {

    //属性 为了存储所有被管理的对象且集合唯一

    private static HashMap<String,Object> beanBox = new HashMap<>();

    //设计一个方法 获取任何一个类的对象
    // 返回值 泛型  参数String（类名）--用反射   <T>T  第一个T是用什么类型地接，第二个T就是返回值类型，它被泛型成第一个T了
    //Bean是一个对象 用static就不用建MySpring的对象了，直接类名调方法
    public static <T>T getBean(String className) {//传递一个类全名哦，类名-->对象是用到了反射
        T obj = null;
        try {
            //1.直接取beanBox里面获取
            obj = (T) beanBox.get(className);
            //注意：用if  实现单例，用泛型  避免再造型
            //2.如果obj是null 证明之前没有创建过这个对象
            if (obj == null) {
                //3.通过类名字获取Class类
                Class clazz = Class.forName(className);
                //4.通过反射产生一个对象
                obj = (T) clazz.newInstance();
                //5.新的对象存入集合
                beanBox.put(className, obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}

