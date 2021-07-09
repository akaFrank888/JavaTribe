package web.servlet;

import domain.ResultInfo;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import service.impl.UserServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/*
    一、该UserServlet类是从RegisterUserServlet，LoginServlet，FindUserServlet，ExitServlet各个Servlet中抽取出来的

    功能：只负责用户的 注册，登录，退出

    二、关于此处的url-pattern（虚拟路径）：
          /user   --->   该Servlet的路径
          /*      --->   方法的名称

    因为要实现UserServlet代替所有关于User的Servlet，即都要访问它，所有用/*

 */

/**
 *      关于User所有请求的Servlet
 */

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 先进行验证码的校验！

        // 1. 获取填写的验证码
        String securityCode = request.getParameter("securityCode");
        // 2. 从session中获取（正确的）验证码
        HttpSession session = request.getSession();
        String checkCodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        // 为保证验证码只能使用一次且避免用户点击返回导致验证码的复用或不能及时生成，应该在验证码获取之后，立刻从session中清除掉该验证码
        /*session.removeAttribute("CHECKCODE_SERVER");*/

        // 3. 比较
        if (checkCodeServer == null || !checkCodeServer.equalsIgnoreCase(securityCode)) {

            // 验证码错误，响应结果给客户端
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");

            // 可直接调用封装json的方法(父类里的)
            this.writeValue(response,info);


            // 直接结束
            return;
        }

        // 1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        // 2. 封装对象（通过BeanUtils）
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // 3.调用service完成注册
        UserService service = new UserServiceImpl();
        boolean flag = service.register(user);
        // 创建返回前端的数据对象info
        ResultInfo info = new ResultInfo();
        // 4. 响应结果
        if (flag) {
            // 注册成功，将用户存入session
            request.getSession().setAttribute("user", user);
            info.setFlag(true);
        } else {
            // 注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }

        // 可直接调用封装json的方法(该类的父类里的，所以可以用this调用)
        this.writeValue(response, info);

    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 通过request中的参数，获取用户名和密码（post请求方式的特点）
        Map<String, String[]> map = request.getParameterMap();
        // 2. 封装user对象
        User user = new User();
        try {
            // populate()会遍历map中的key，如果bean(user)中的属性有key，则把这个key对应的value赋值给bean的属性
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // 3.调用Service查询    （UserService是一个接口，new的是一个实现类）（类似于“这个人是一个老师”）
        UserService service = new UserServiceImpl();
        User u = service.login(user);
        // 4. 判断用户是否为null
        ResultInfo info = new ResultInfo();
        if (u == null) {
            // 用户名账户或密码错误
            info.setFlag(false);
            info.setErrorMsg(" 用户名账户或密码错误");
        } else {
            // 登录成功
            // 服务器用session保存登录的u对象！！  别忘了
            // （session用request获取）
            request.getSession().setAttribute("user", u);

            info.setFlag(true);
        }

        // 5. 响应数据 可直接调用将对象封装成json的方法(this.是因为调用父类里的)
        this.writeValue(response,info);
    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 销毁session
        request.getSession().invalidate();

        // 2. 跳转到首页（response的重定向）
        // 注意重定向中路径的写法：request.getContextPath()获得绝对路径，再加上虚拟路径（不要忘/）
        response.sendRedirect(request.getContextPath()+"/visitor.html");
    }

    public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 因为在loginServlet中已经将登录成功的user以user为键存入session了
        // 所以在这可以从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        // 可直接调用封装json的方法(父类里的)
        this.writeValue(response,user);
    }
}
