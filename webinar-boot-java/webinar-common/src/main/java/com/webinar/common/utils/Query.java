package com.webinar.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 *
 * @author
 * @since 2.0.0 2017-03-14
 */
public class Query<T> extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    /**
     * mybatis-plus分页参数
     */
    private IPage<T> page;
    /**
     * 当前页码
     */
    private int currPage = 1;
    /**
     * 每页条数
     */
    private int limit = 10;
    private final static String LIMITKEY = "limit";
    /**
     * 构造函数
     * @param params UI参数
     */
    public Query(Map<String, Object> params){
        this.putAll(params);

        //分页参数
        if(params.get("page") != null){
            currPage = Integer.parseInt((String)params.get("page"));
        }
        if(params.get(LIMITKEY) != null){
            limit = Integer.parseInt((String)params.get(LIMITKEY));
        }

        this.put("offset", (currPage - 1) * limit);
        this.put("page", currPage);
        this.put(LIMITKEY, limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String)params.get("sidx"));
        String order = SQLFilter.sqlInject((String)params.get("order"));
        this.put("sidx", sidx);
        this.put("order", order);

        //mybatis-plus分页
        this.page = new Page<>(currPage, limit);

    }

    /**
     * 获取页面对象
     * @return
     */
    public IPage<T> getPage() {
        return page;
    }

    /**
     * 获取当前页码
     * @return
     */
    public int getCurrPage() {
        return currPage;
    }

    public int getLimit() {
        return limit;
    }
}

