package com.webinar.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.webinar.common.core.domain.entity.SysMenuD;
import com.webinar.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author webinar
* @description 针对表【sys_menuD(菜单)】的数据库操作Mapper
* @createDate 2024-07-31 13:59:08
* @Entity generator.domain.SysMenud
*/
public interface SysMenudMapper extends BaseMapper<SysMenuD> {

    /**
     * 获取不包括按钮的菜单列表
     * @return
     */
    @Select("SELECT m.* from sys_menuD m  where  m.system_id=2 " +
            "  ORDER BY m.order_num,m.menu_id ASC")
    List<SysMenuD> queryAllMenu();

    @Select("SELECT m.* from sys_menuD m  where  m.system_id=2 and m.type<>2 " +
            "  ORDER BY m.order_num,m.menu_id ASC")
    List<SysMenuD> queryAllMenuList();


    @Select("SELECT m.* from sys_menuD m " +
            " LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            " LEFT JOIN sys_user_role ur on ur.role_id = rm.role_id " +
            " where m.type<>2 and  m.system_id=2 and ur.user_id = #{userId} ORDER BY m.order_num,m.menu_id ASC")
    List<SysMenuD> queryMenuListByUserId(Long userId);

    List<String> selectMenuPermsByRoleId(Long roleId);

    List<SysMenuD> getMenuAll();

    List<SysMenuD> getMenuByUserId(SysUser user);

    List<String> selectMenuPermsByUserId(Long userId);

    List<SysMenuD> queryNotButtonList();

}




