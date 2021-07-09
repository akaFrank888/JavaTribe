package service;

import domain.Blog;
import domain.PageBean;
import domain.UserInfo;

/**
 * 实现分页查询
 */
public interface BlogService {

    // 设计一个方法    显示一页的博客（在Servlet里已经将接收到的String参数-->int）

    public PageBean<Blog> pageQuery(int currentPage, int pageSize,String content);

    // 设计一个方法    显示一页我的博客

    public PageBean<Blog> pageQueryOfMine(int currentPage, int pageSize,String account);

    // 设计一个方法   在博客页面显示作者博客

    public Blog showOneByBlog(String date);

    // 设计一个方法   在博客页面显示作者资料

    public UserInfo showOneByUser(String account);

    // 设计一个方法    在数据库中保存一则博客

    public boolean save(Blog blog);
}
