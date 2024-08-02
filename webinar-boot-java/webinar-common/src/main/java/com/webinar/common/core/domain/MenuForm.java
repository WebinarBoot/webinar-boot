package com.webinar.common.core.domain;

import lombok.Data;

@Data
public class MenuForm {
    private Long menuId;

    private Long menuMetaId;

    private String menuName;

    private String componentName;

    private String type;

    private Long parentId;

    private Integer orderNo;

    private String icon;

    private String component;

    private String path;

    private String permission;


    /**
     *   meta 属性
     */
    private String keepalive;

    private String show;

    private Integer externalLink;

    private String frameSrc;

    /**
     * 功能类型
     */
    private Integer actionType;
}

