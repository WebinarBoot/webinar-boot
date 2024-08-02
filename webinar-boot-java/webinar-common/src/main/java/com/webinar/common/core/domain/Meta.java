package com.webinar.common.core.domain;

import lombok.Data;

@Data
public class Meta {
    /**
     * 路由权限名称
     *
     */
    private String title = "";

    /**
     * 是否固定标签
     *
     */
    private Boolean affix = true;
    /**
     * 图标
     *
     */
    private String icon = "";
    /**
     * 是否隐藏菜单列表显示
     */
    private Boolean hideMenu = true;

    /**
     * 外链项目地址
     */
    private String frameSrc;
}
