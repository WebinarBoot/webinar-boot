package com.webinar.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.webinar.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

@TableName("sys_menuD_meta")
@Data
public class SysMenuDMeta extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识
     *
     */
    @TableId(value = "menu_meta_id", type = IdType.AUTO)
    private Long menuMetaId;

    /**
     * 目录表ID
     *
     */
    private Long menuId;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 路由权限名称
     *
     */
    private String title;

    /**
     * 是否忽略权限，只在权限模式为Role的时候有效
     *
     */
    private Boolean ignoreAuth;

    /**
     * 可以访问的角色，只在权限模式为Role的时候有效
     *
     */
    private String roles;

    /**
     * 是否忽略KeepAlive缓存
     *
     */
    private Boolean ignoreKeepAlive;

    /**
     * 是否固定标签
     *
     */
    private Boolean affix;

    /**
     * 内嵌iframe地址
     *
     */
    private String frameSrc;

    /**
     * 指定该路由切换的动画名
     *
     */
    private String transitionName;

    /**
     * 隐藏该路由在面包屑上面的显示
     *
     */
    private Boolean hideBreadcrumb;

    /**
     * 如果该路由会携带参数，且需要在tab页上面显示。则需要设置为true
     *
     */
    private Boolean carryParam;

    /**
     * 隐藏所有子菜单
     *
     */
    private Boolean hideChildreninMenu;

    /**
     * 当前激活的菜单。用于配置详情页时左侧激活的菜单路径
     *
     */
    private String currentActiveMenu;

    /**
     * 当前路由不再标签页显示
     *
     */
    private Boolean hideTab;

    /**
     * 当前路由不再菜单显示
     *
     */
    private Boolean hideMenu;

    /**
     * 图标
     *
     */
    private String icon;

}
