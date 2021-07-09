package web.servlet;

import domain.Blog;
import domain.PageBean;
import domain.ResultInfo;
import domain.UserInfo;
import org.apache.commons.beanutils.BeanUtils;
import service.BlogService;
import service.impl.BlogServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *      关于Blog所有请求的Servlet
 */

@WebServlet("/blog/*")
public class BlogServlet extends BaseServlet {

    // 设计一个方法    显示一页的博客

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String content = request.getParameter("content");
        System.out.println(content);

        // 2. 处理参数（因为接收到的参数的形式是String，所以要转成int）
        int currentPage;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            // 当前页码：如果不传递，则默认为第一页
            currentPage = 1;
        }

        int pageSize;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            // 每页显示条数，如果不传递，则默认每页显示5条记录
            pageSize = 5;
        }

        // 3. 调用service查询PageBean对象
        BlogService service = new BlogServiceImpl();
        PageBean<Blog> pb = service.pageQuery(currentPage, pageSize,content);

        System.out.println("写给浏览器之前看看pb里的内容："+pb.getTotalPage()+"&"+pb.getTotalCount());

        // 4. 将pb序列化为json，传给浏览器
        writeValue(response,pb);
    }

    // 设计一个方法    显示一页 我的 博客

    public void pageQueryOfMine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 接收参数
        String account = request.getParameter("account");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        // 2. 处理参数（因为接收到的参数的形式是String，所以要转成int）
        int currentPage;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            // 当前页码：如果不传递，则默认为第一页
            currentPage = 1;
        }

        int pageSize;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            // 每页显示条数，如果不传递，则默认每页显示5条记录
            pageSize = 5;
        }

        // 3. 调用service查询PageBean对象
        BlogService service = new BlogServiceImpl();
        PageBean<Blog> pb = service.pageQueryOfMine(currentPage, pageSize,account);

        System.out.println("写给浏览器之前看看pb里的内容："+pb.getTotalPage()+"&"+pb.getTotalCount());

        // 4. 将pb序列化为json，传给浏览器
        writeValue(response,pb);
    }

    // 设计一个方法   将被点击的博客的date存入session  为了以后通过session来获得date进而获得blog

    public void saveDateAndAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 先接收参数
        String date = request.getParameter("date");
        String account = request.getParameter("account");
        // 再存入session

        request.getSession().setAttribute("date",date);
        request.getSession().setAttribute("account",account);
        // 向浏览器相应
        ResultInfo info = new ResultInfo();
        if (date != null && account != null) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
        }
        this.writeValue(response, info);
    }

    // 设计一个方法   在博客页面显示作者博客

    public void showOneByBlog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 因为在clickShowOne中已经date以date为键存入session了
        // 所以在这可以从session中获取
        String date = (String)request.getSession().getAttribute("date");
        // 调用service查询blog
        BlogService service = new BlogServiceImpl();
        Blog blog = service.showOneByBlog(date);
        // 向前端响应结果
        this.writeValue(response,blog);

    }

    // 设计一个方法   在博客页面显示作者资料

    public void showOneByUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 因为在clickShowOne中已经date以date为键存入session了
        // 所以在这可以从session中获取
        String account = (String)request.getSession().getAttribute("account");
        // 调用service查询blog
        BlogService service = new BlogServiceImpl();
        UserInfo user = service.showOneByUser(account);
        // 向前端响应结果
        this.writeValue(response,user);

    }


    // 设计一个方法   发布一条博客

    public void publish(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 接收参数
        Map<String, String[]> map = request.getParameterMap();
        // 封装blog对象
        Blog blog = new Blog();
        try {
            BeanUtils.populate(blog, map);
            System.out.println(blog.getTitle());
            System.out.println(blog.getContent());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // 调用service
        BlogService service = new BlogServiceImpl();
        boolean flag = service.save(blog);
        // 向前端响应结果
        ResultInfo info = new ResultInfo();
        if (flag) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("博客内容不完整，发布失败");
        }
        this.writeValue(response,info);
    }
}
