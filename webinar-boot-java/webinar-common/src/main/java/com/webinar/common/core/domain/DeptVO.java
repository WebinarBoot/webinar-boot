package com.webinar.common.core.domain;

import lombok.Data;

import java.util.List;

@Data
public class DeptVO {
    /**
     * key值
     */
    private String key;
    /**
     * 节点名称
     */
    private String title;
    /**
     * 节点图标
     */
    private String icon;
    /**
     * 子节点
     */
    private List<DeptVO> children;
}
