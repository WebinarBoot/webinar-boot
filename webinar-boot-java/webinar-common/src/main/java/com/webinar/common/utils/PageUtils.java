package com.webinar.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import com.webinar.common.core.page.PageDomain;
import com.webinar.common.core.page.TableSupport;
import com.webinar.common.utils.sql.SqlUtil;

import java.util.List;

/**
 * 分页工具类
 * 
 * @author webinar
 */
public class PageUtils extends PageHelper
{
    private static final long serialVersionUID = 1L;
    //总记录数
    private long totalCount;
    //总记录数
    private long count;
    //每页记录数
    private long pageSize;
    //总页数
    private long totalPage;
    //当前页数
    private long currPage;
    //当前页数
    private long page;
    //列表数据
    private transient List<?> list;

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currPage    当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.count=totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.page=currPage;
        this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
    }

    /**
     * 分页
     */
    public PageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = page.getTotal();
        this.count=page.getTotal();
        this.pageSize = page.getSize();
        this.currPage = page.getCurrent();
        this.totalPage = page.getPages();
        this.page=page.getPages();
    }

    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public long getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
