package service.impl;

import dao.BlogDao;
import dao.impl.BlogDaoImpl;
import domain.Blog;
import domain.PageBean;
import domain.UserInfo;
import service.BlogService;

public class BlogServiceImpl implements BlogService {

    private BlogDao dao = new BlogDaoImpl();

    // 设计一个方法   查询一页的博客情况

    @Override
    public PageBean<Blog> pageQuery(int currentPage, int pageSize,String content) {

        // 封装一个PageBean，然后返回
        PageBean<Blog> pb = new PageBean<>();

        // 设置pb的5个属性
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = dao.findTotalCount(content);
        System.out.println("这是totalCount = "+totalCount);
        pb.setTotalCount(totalCount);

        // 公式： beginIndex = （当前页-1）*每页条数
        int beginIndex = (currentPage - 1) * pageSize;
        pb.setList(dao.findByPage(beginIndex, pageSize,content));

        // 巧用条件运算符   --->    ? :
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        System.out.println("这是totalPage = "+totalPage);

        return pb;
    }

    // 设计一个方法   查询一页的我的博客的情况

    @Override
    public PageBean<Blog> pageQueryOfMine(int currentPage, int pageSize,String account) {
        // 封装一个PageBean，然后返回
        PageBean<Blog> pb = new PageBean<>();

        // 设置pb的5个属性
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCountOfMine = dao.findTotalCountOfMine(account);
        System.out.println("这是account："+account);
        System.out.println("这是totalCount = "+totalCountOfMine);
        pb.setTotalCount(totalCountOfMine);

        // 公式： beginIndex = （当前页-1）*每页条数
        int beginIndex = (currentPage - 1) * pageSize;
        pb.setList(dao.findByPageOfMine(beginIndex, pageSize,account));


        // 巧用条件运算符   --->    ? :
        int totalPage = totalCountOfMine % pageSize == 0 ? totalCountOfMine / pageSize : (totalCountOfMine / pageSize) + 1;
        pb.setTotalPage(totalPage);
        System.out.println("这是totalPage = "+totalPage);

        return pb;
    }

    @Override
    public Blog showOneByBlog(String date) {
        return dao.showOneByBlog(date);
    }

    @Override
    public UserInfo showOneByUser(String account) {
        return dao.showOneByUser(account);
    }

    @Override
    public boolean save(Blog blog) {

        // 如果博客的 title，content其一未填写，则保存失败

        if (blog.getTitle() == null || blog.getContent() == null) {
            return false;
        }
        dao.save(blog);
        return true;
    }
}
