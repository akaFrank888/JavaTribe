package domain;

import java.util.List;


/**
 * 分页对象   总共5个属性
 */
public class PageBean<T> {


    // 总记录数（通过数据库查询）

    private int totalCount;

    // 总页数（能算出来）

    private int totalPage;

    // 当前页码（通过浏览器传过来）

    private int currentPage;

    // 每页显示的条数（通过浏览器传过来）

    private int pageSize;

    // 每页显示的数据集合（通过数据库查询）

    private List<T> list;



    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
