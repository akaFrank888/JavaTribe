package web.servlet;

import domain.ResultInfo;
import domain.UserInfo;
import org.apache.commons.beanutils.BeanUtils;
import service.UserInfoService;
import service.impl.UserInfoServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 *      关于UserInfo（用户资料卡）所有请求的Servlet
 */

@WebServlet("/userInfo/*")
public class UserInfoServlet extends BaseServlet {
    public void fillIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 获取数据  （request是从前端发来的ajax请求）
        Map<String, String[]> map = request.getParameterMap();
        // 2. 封装对象
        UserInfo userInfo = new UserInfo();
        try {
            BeanUtils.populate(userInfo, map);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // 3. 调用service完成存入数据库
        UserInfoService service = new UserInfoServiceImpl();
        boolean flag = service.fillIn(userInfo);
        // 4. 向前端响应结果
        ResultInfo info = new ResultInfo();
        if (flag) {
            // 成功存入数据库
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("未完整填写资料卡，修改失败");
        }

        // 可直接调用封装json的方法(父类里的)
        this.writeValue(response, info);
    }
    public void showUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取参数（该用户的账号）
        String account = request.getParameter("accountName");

        System.out.println("request获取的account："+account);

        // 通过service判断该用户是否填过资料卡
        UserInfoService service = new UserInfoServiceImpl();
        UserInfo userInfo = service.showUserInfo(account);
        // 创建resultInfo对象，反馈信息
        ResultInfo resultInfo = new ResultInfo();
        if (userInfo == null) {
            // 用户没填过资料卡
            resultInfo.setFlag(false);
        } else {
            // 填过
            resultInfo.setFlag(true);
            resultInfo.setDataObj(userInfo);
        }
        this.writeValue(response,resultInfo);
    }
}
