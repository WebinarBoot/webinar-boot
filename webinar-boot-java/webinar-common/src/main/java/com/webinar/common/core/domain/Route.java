package com.webinar.common.core.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Route {
    /**
     * 路径
     */
    private String path="";
    /**
     * 命名路由-路由名称
     */
    private String name="";
    /**
     * 页面组件
     */
    private String component="";
    /**
     * 重定向地址
     */
    private String redirect="";
    /**
     * 元数据
     */
    private Meta meta=new Meta();

    /**
     * 嵌套子路由
     */
    private List<Route> children=new ArrayList();
}
