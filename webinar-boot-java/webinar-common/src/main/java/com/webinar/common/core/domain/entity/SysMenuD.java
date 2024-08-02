package com.webinar.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.webinar.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@Data
@TableName("sys_menuD")
public class SysMenuD extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String componentName;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型 0：目录 1：菜单 2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 功能类型
     */
    private Integer actionType;
    /**
     * 菜单选中设置
     */
    private String sideMenuId;
    /**
     * 系统ID
     */
    private Integer systemId;
    /**
     * 扩展url，用于显示左边目录树
     */
    private String extUrl;

    /**
     * ztree属性
     */
    @TableField(exist = false)
    private Boolean open;

    /**
     * ztree属性
     */
    @TableField(exist = false)
    private Boolean checked;
    /**
     * @TableField(exist = false)
     * private List<?> list;
     */

    // 以下：路由新增属性
    /**
     * 对应组件
     */
    private String component;

    @TableField(exist = false)
    private List<SysMenuD> children;

    /**
     * 元数据
     */
    @TableField(exist = false)
    private SysMenuDMeta meta;

    /**
     * 按钮样式为true则显示小圆点
     *
     */
    private Integer tagDot;

    /**
     * 按钮样式文字
     *
     */
    private String tagContent;

    /**
     * 按钮类型
     *
     */
    private String tagType;
    /**
     * 路由组件传递参数
     *
     */
    private String props;

    /**
     * 路由别名
     *
     */
    private String alias;

    /**
     * 匹配规则是否大小写敏感？(默认值：false)
     *
     */
    private Integer caseSensitive;
    /**
     * 路由路径
     *
     */
    private String path;
    /**
     * 编译正则的选项
     *
     */
    private Integer pathToRegexpOptions;
    /**
     * 重定向路径
     *
     */
    private String redirect;

    /************************************扩展字段************************************/
    /**
     * ID
     */
    @TableField(exist = false)
    private String id;

    /**
     * 目录名称
     */
    @TableField(exist = false)
    private String menuName;

    /**
     * 排序
     */
    @TableField(exist = false)
    private Integer orderNo;

    /**
     * 权限代码
     */
    @TableField(exist = false)
    private String permission;

    /**
     * 状态
     */
    @TableField(exist = false)
    private String status;

    /************************************Meta属性************************************/
    /**
     * metaId
     */
    @TableField(exist = false)
    private Long menuMetaId;
    /**

     /**
     * 是否缓存
     */
    @TableField(exist = false)
    private String keepalive;
    /**
     * 是否显示
     */
    @TableField(exist = false)
    private String show;

    /**
     * 外链地址
     * @return
     */
    @TableField(exist = false)
    private String frameSrc;
}
