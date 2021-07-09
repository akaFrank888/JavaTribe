package dao;
import domain.Blog;
import domain.UserInfo;
import java.util.List;



public interface BlogDao {

    // 设计一个方法   查询总博客数
    // 会有两种情况调用该方法：
    //            情况一、普通的展示博客内容
    //            情况二、通过搜索功能展示博客内容

    public int findTotalCount(String content);

    // 设计一个方法   查询该用户的博客数

    public int findTotalCountOfMine(String account);

    // 设计一个方法   查询当前页的博客集合（因为sql语句中是beginIndex，所以此处参数不写currentPage）
    // 会有两种情况调用该方法：
    //            情况一、普通的展示博客内容
    //            情况二、通过搜索功能展示博客内容

    public List<Blog> findByPage(int beginIndex, int pageSize, String content);

    // 设计一个方法   查询当前页的我的博客集合

    public List<Blog> findByPageOfMine(int beginIndex, int pageSize,String account);

    // 设计一个方法   在博客页面显示作者博客

    public Blog showOneByBlog(String date);

    // 设计一个方法   在博客页面显示作者博客

    public UserInfo showOneByUser(String account);

    // 设计一个方法   在数据库中保存一则博客

    public void save(Blog blog);
}
