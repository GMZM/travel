/**
 * 文件名：PageBean
 * 作者：liuzeming
 * 时间：2019/4/15 15:05
 * 描述：
 */

package cn.itcast.travel.domain;

import java.util.List;

public class PageBean<T> {
    private int totalCount;//总条数
    private int totalPage;//总页数
    private int currentPage;//当前页面
    private int pageSize;//每页的条数

    private List<T> list;//每页显示的数据集合

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

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
