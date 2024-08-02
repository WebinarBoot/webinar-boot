package com.webinar.web.controller.system;

import com.alibaba.fastjson2.JSON;
import com.webinar.common.annotation.Log;
import com.webinar.common.core.controller.BaseController;
import com.webinar.common.core.domain.*;
import com.webinar.common.core.domain.entity.SysMenuD;
import com.webinar.common.core.domain.entity.SysRole;
import com.webinar.common.core.domain.entity.SysUser;
import com.webinar.common.enums.BusinessType;
import com.webinar.common.utils.MapUtils;
import com.webinar.common.utils.SecurityUtils;
import com.webinar.common.utils.StringUtils;
import com.webinar.system.service.ISysRoleService;
import com.webinar.system.service.SysMenudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 * 
 * @author webinar
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private SysMenudService sysMenudService;
    @Autowired
    private ISysRoleService roleService;

    @GetMapping("getMenuListById")
    public AjaxResult getMenuListById(@ApiIgnore @RequestParam("userId") long userId) {
        List<Route> routes;
        List<SysMenuD> list=sysMenudService.queryRouteList(null,userId);
        routes= JSON.parseArray(JSON.toJSONString(list), Route.class);
        return success(routes);
    }
    @GetMapping("/getMenuTopList")
    public AjaxResult getMenuList() {
        List<SysMenuD> routes;
        List<SysMenuD> list=sysMenudService.getMenuList(null,0L);
        routes= JSON.parseArray(JSON.toJSONString(list), SysMenuD.class);
        SysMenuD entity = new SysMenuD();
        entity.setMenuName("一级目录");
        entity.setMenuId(0L);
        routes.add(0, entity);
        return success(routes);
    }

    @PostMapping("/saveMenu")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理-新增", businessType = BusinessType.INSERT)
    public AjaxResult saveMenu(@RequestBody MenuForm menu) {
        return sysMenudService.saveMenu(menu) ? success("保存菜单成功") : error("保存菜单失败");
    }

    @PostMapping("/updateMenu")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理-修改", businessType = BusinessType.UPDATE)
    public AjaxResult updateLevelMark(@RequestBody MenuForm menu) {
        return sysMenudService.updateMenu(menu) ? success("修改菜单成功") : error("修改菜单失败");
    }

    @PostMapping("/deleteMenu")
    @PreAuthorize("@ss.hasPermi('system:menu:delete')")
    @Log(title = "菜单管理-删除", businessType = BusinessType.DELETE)
    public AjaxResult deleteMenu(@RequestBody MenuForm menu) {
        return sysMenudService.deleteMenu(menu.getMenuId().toString()) ? success("删除成功") : error("删除失败");
    }


    @GetMapping("/getMenuListByParams")
    public AjaxResult getMenuListByParams(MenuParams form) {
        Map<String, Object> PromiseResult = new MapUtils();

        // 构建查询条件
        Map<String, Object> params = new MapUtils();
        params.put("menuName", form.getMenuName());
        params.put("status", form.getStatus());
        if (form.getPage() != null && form.getPageSize() != null){
            params.put("page",form.getPage().toString());
            params.put("limit",form.getPageSize().toString());
        }
        List<SysMenuD> menus = sysMenudService.getMenuListByParams(params);

        PromiseResult.put("items", menus);
        PromiseResult.put("total", menus.size());

        return success(PromiseResult);
    }


    /**
     * 获取当前用户菜单权限
     */
    @GetMapping("/getPermCode")
    public AjaxResult getPermCode()
    {
        List<String> perms = new ArrayList<>();
        List<SysMenuD> sysMenus = new ArrayList<>();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        //判断是否超级管理员
        SysRole sysRole = roleService.getRoloById(user);
        if(sysRole.getRoleKey().equals("admin")){
            sysMenus = sysMenudService.getMenuAll();
        }else {
            sysMenus = sysMenudService.getMenuByUserId(user);
        }
        for (SysMenuD sysMenu : sysMenus) {
            if(StringUtils.isNotNull(sysMenu.getPerms())){
                perms.add(sysMenu.getPerms());
            }
        }
        return success(perms);
    }

    @GetMapping("/getMenuList")
    public AjaxResult getAllMenuList(){
        List<SysMenuD> buttonList = sysMenudService.getAllMenuList();
        return success(buttonList);
    }
}