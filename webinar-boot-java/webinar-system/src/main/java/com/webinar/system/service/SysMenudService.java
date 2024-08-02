package com.webinar.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.webinar.common.core.domain.MenuForm;
import com.webinar.common.core.domain.entity.SysMenuD;
import com.webinar.common.core.domain.entity.SysUser;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author webinar
* @description 针对表【sys_menuD(菜单)】的数据库操作Service
* @createDate 2024-07-31 13:59:08
*/
public interface SysMenudService extends IService<SysMenuD> {

    List<SysMenuD> getAllMenuList();

    List<SysMenuD> queryRouteList(Map<String, Object> params, long userId);

    Set<String> selectMenuPermsByRoleId(Long roleId);

    Collection<String> selectMenuPermsByUserId(Long userId);

    List<SysMenuD> getMenuAll();

    List<SysMenuD> getMenuByUserId(SysUser user);

    List<SysMenuD> getMenuListByParams(Map<String, Object> params);

    List<SysMenuD> getMenuList(Map<String, Object> params, Long userId);

    boolean saveMenu(MenuForm menu);

    boolean updateMenu(MenuForm menu);

    boolean deleteMenu(String menuId);
}
