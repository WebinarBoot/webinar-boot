package com.webinar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.webinar.common.core.domain.MenuForm;
import com.webinar.common.core.domain.entity.SysMenuD;
import com.webinar.common.core.domain.entity.SysMenuDMeta;
import com.webinar.common.core.domain.entity.SysUser;
import com.webinar.common.utils.MapUtils;
import com.webinar.common.utils.StringUtils;
import com.webinar.system.domain.SysRoleMenu;
import com.webinar.system.mapper.SysMenudMapper;
import com.webinar.system.mapper.SysMenudMetaMapper;
import com.webinar.system.mapper.SysRoleMenuMapper;
import com.webinar.system.service.SysMenudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author webinar
* @description 针对表【sys_menuD(菜单)】的数据库操作Service实现
* @createDate 2024-07-31 13:59:08
*/
@Service
public class SysMenudServiceImpl extends ServiceImpl<SysMenudMapper, SysMenuD>
    implements SysMenudService {
    @Autowired
    private SysMenudMapper sysMenudMapper;
    @Autowired
    private SysMenudMetaMapper sysMenuMetaDao;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenuD> getMenuAll() {

        return sysMenudMapper.getMenuAll();
    }

    @Override
    public boolean saveMenu(MenuForm menu) {
        // 创建菜单对象
        SysMenuD menuEntity = new SysMenuD();
        menuEntity.setName(menu.getMenuName());
        menuEntity.setParentId(menu.getParentId());
        menuEntity.setType(Integer.parseInt(menu.getType()));
        menuEntity.setActionType(menu.getActionType());
        menuEntity.setOrderNum(menu.getOrderNo());
        menuEntity.setSystemId(2);
        if ("0".equals(menu.getType())) {
            menuEntity.setComponent("LAYOUT");
        } else {
            menuEntity.setComponent(menu.getComponent());
            menuEntity.setPerms(menu.getPermission());
        }
        menuEntity.setPath(menu.getPath());

        int save = sysMenudMapper.insert(menuEntity);
        if (save>0) {
            SysMenuDMeta metaEntity = new SysMenuDMeta();
            metaEntity.setMenuId(menuEntity.getMenuId());
            metaEntity.setTitle(menu.getMenuName());
            metaEntity.setComponentName(menu.getComponentName());
            metaEntity.setIgnoreAuth(true);
            metaEntity.setIgnoreKeepAlive("0".equals(menu.getKeepalive()) ? true : false);
            metaEntity.setAffix(false);
            metaEntity.setHideMenu("0".equals(menu.getShow()) ? false : true);
            metaEntity.setIcon(menu.getIcon());
            metaEntity.setFrameSrc(menu.getFrameSrc());
            sysMenuMetaDao.insert(metaEntity);
        }
        return save>0?true:false;
    }

    @Override
    public boolean deleteMenu(String menuId) {
        Boolean re = false;
        List<SysMenuD> ls = this.list(
                new LambdaQueryWrapper<SysMenuD>()
                        .eq(StringUtils.isNotBlank(menuId),SysMenuD::getParentId, menuId)
                        .orderByDesc(SysMenuD::getOrderNum)
        );;
        if (ls.size() > 0) {
            throw new RuntimeException("请先删除子节点");
        } else {
            re = this.removeById(menuId);
            sysMenuMetaDao.delete(new LambdaQueryWrapper<SysMenuDMeta>().eq(SysMenuDMeta::getMenuId, menuId));
            if (re) {
                Map<String, Object> params1 = new MapUtils();
                params1.put("menu_id", menuId.toString());
                List<SysRoleMenu> menuRoleList = sysRoleMenuMapper.selectByMap(params1);
                for (SysRoleMenu item : menuRoleList) {
                    sysRoleMenuMapper.deleteById(item);
                }

            }
        }
        return re;
    }

    @Override
    public boolean updateMenu(MenuForm menu) {
        if (menu.getParentId().equals(menu.getMenuId())) {
            throw new RuntimeException("上级目录无法选择自己！");
        }

        // 创建菜单对象
        SysMenuD menuEntity = new SysMenuD();
        menuEntity.setMenuId(menu.getMenuId());
        menuEntity.setName(menu.getMenuName());
        menuEntity.setParentId(menu.getParentId());
        menuEntity.setType(Integer.parseInt(menu.getType()));
        menuEntity.setActionType(menu.getActionType());
        menuEntity.setOrderNum(menu.getOrderNo());
        menuEntity.setSystemId(2);
        if ("0".equals(menu.getType())) {
            menuEntity.setComponent("LAYOUT");
        } else {
            menuEntity.setComponent(menu.getComponent());
            menuEntity.setPerms(menu.getPermission());
        }
        menuEntity.setPath(menu.getPath());

        boolean update = this.updateById(menuEntity);
        if (update) {
            SysMenuDMeta metaEntity = new SysMenuDMeta();
            metaEntity.setMenuMetaId(menu.getMenuMetaId());
            metaEntity.setMenuId(menuEntity.getMenuId());
            metaEntity.setTitle(menu.getMenuName());
            metaEntity.setComponentName(menu.getComponentName());
            metaEntity.setIgnoreKeepAlive("0".equals(menu.getKeepalive()) ? false : true);
            metaEntity.setAffix(false);
            metaEntity.setHideMenu("0".equals(menu.getShow()) ? false : true);
            metaEntity.setIcon(menu.getIcon());
            metaEntity.setFrameSrc(menu.getFrameSrc());
            sysMenuMetaDao.updateById(metaEntity);
        }
        return update;
    }

    @Override
    public List<SysMenuD> getMenuList(Map<String, Object> params, Long userId) {
        // 查询当前登录用户路由菜单(判断是否超管)
        List<SysMenuD> menuEntityList = sysMenudMapper.queryNotButtonList();
        List<SysMenuD> list = getTreeMapForList(menuEntityList);
        return list;
    }

    @Override
    public List<SysMenuD> getMenuListByParams(Map<String, Object> params) {
        String menuName = (String) params.get("menuName");

        List<SysMenuD> menus = sysMenudMapper.selectList(
                new QueryWrapper<SysMenuD>()
                        .like(StringUtils.isNotBlank(menuName), "name", menuName)
                        .orderByAsc("order_num")
                        .eq("system_id",2)
        );
        List<SysMenuD> list;
        if (!StringUtils.isNotBlank(menuName)) {
            list = getTreeMapForList(menus);
        } else {
            for (SysMenuD item : menus){
                SysMenuDMeta meta=sysMenuMetaDao.queryMetaByMenuId(item.getMenuId());
                item.setId(String.valueOf(item.getMenuId()));
                item.setMenuName(meta.getTitle());
                item.setOrderNo(item.getOrderNum());
                item.setPermission(item.getPerms());
                item.setIcon(meta.getIcon());
                // meta 属性赋值
                item.setMenuMetaId(meta.getMenuMetaId());
                item.setComponentName(meta.getComponentName());
                if (meta.getHideMenu() != null && meta.getIgnoreKeepAlive() != null){
                    if (meta.getIgnoreKeepAlive()){
                        item.setKeepalive("1");
                    }else{
                        item.setKeepalive("0");
                    }
                    if (meta.getHideMenu()){
                        item.setShow("1");
                    }else{
                        item.setShow("0");
                    }
                }
            }
            list = menus;
        }

        return list;
    }

    @Override
    public List<SysMenuD> getMenuByUserId(SysUser user) {

        return sysMenudMapper.getMenuByUserId(user);
    }

    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = sysMenudMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId)
    {
        List<String> perms = sysMenudMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }


    @Override
    public List<SysMenuD> queryRouteList(Map<String, Object> params, long userId) {
        // 查询当前登录用户路由菜单(判断是否超管)
        List<SysMenuD> menuEntityList =
                userId == 1 ? sysMenudMapper.queryAllMenuList() : sysMenudMapper.queryMenuListByUserId(userId);
        List<SysMenuD> list = getTreeMap(menuEntityList);
        return list;
    }

    private List<SysMenuD> getTreeMap(List<SysMenuD> treeNodes) {
        List<SysMenuD> trees = new ArrayList<>();
        for (SysMenuD treeNode : treeNodes) {
            if (treeNode.getParentId() == 0) {
                trees.add(findChildren(treeNode, treeNodes));
            }
            treeNode.setMeta(sysMenuMetaDao.queryMetaByMenuId(treeNode.getMenuId()));
        }
        return trees;
    }
    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public SysMenuD findChildren(SysMenuD treeNode, List<SysMenuD> treeNodes) {
        for (SysMenuD it : treeNodes) {
            if (treeNode.getMenuId().longValue() == it.getParentId().longValue()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.setMeta(sysMenuMetaDao.queryMetaByMenuId(treeNode.getMenuId()));
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
    @Override
    public List<SysMenuD> getAllMenuList() {
        List<SysMenuD> menuEntityList = sysMenudMapper.queryAllMenu();
        List<SysMenuD> list = getTreeMapForList(menuEntityList);
        return list;
    }


    private List<SysMenuD> getTreeMapForList(List<SysMenuD> treeNodes) {
        List<SysMenuD> trees = new ArrayList<>();
        for (SysMenuD treeNode : treeNodes) {
            if (treeNode.getParentId() == 0) {
                trees.add(findChildrenForList(treeNode, treeNodes));
            }

            SysMenuDMeta meta=sysMenuMetaDao.queryMetaByMenuId(treeNode.getMenuId());
            treeNode.setId(String.valueOf(treeNode.getMenuId()));
            treeNode.setMenuName(meta.getTitle());
            treeNode.setOrderNo(treeNode.getOrderNum());
            treeNode.setPermission(treeNode.getPerms());
            treeNode.setIcon(meta.getIcon());
            // meta 属性赋值
            treeNode.setMenuMetaId(meta.getMenuMetaId());
            if (meta.getHideMenu() != null && meta.getIgnoreKeepAlive() != null){
                if (meta.getIgnoreKeepAlive()){
                    treeNode.setKeepalive("1");
                }else{
                    treeNode.setKeepalive("0");
                }
                if (meta.getHideMenu()){
                    treeNode.setShow("1");
                }else{
                    treeNode.setShow("0");
                }
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public SysMenuD findChildrenForList(SysMenuD treeNode, List<SysMenuD> treeNodes) {
        for (SysMenuD it : treeNodes) {
            if (treeNode.getMenuId().longValue() == it.getParentId().longValue()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                SysMenuDMeta meta=sysMenuMetaDao.queryMetaByMenuId(treeNode.getMenuId());
                treeNode.setId(String.valueOf(treeNode.getMenuId()));
                treeNode.setMenuName(meta.getTitle());
                treeNode.setOrderNo(treeNode.getOrderNum());
                treeNode.setPermission(treeNode.getPerms());
                treeNode.setIcon(meta.getIcon());
                // meta 属性赋值
                treeNode.setMenuMetaId(meta.getMenuMetaId());
                if (meta.getHideMenu() != null && meta.getIgnoreKeepAlive() != null){
                    if (meta.getIgnoreKeepAlive()){
                        treeNode.setKeepalive("1");
                    }else{
                        treeNode.setKeepalive("0");
                    }
                    if (meta.getHideMenu()){
                        treeNode.setShow("1");
                    }else{
                        treeNode.setShow("0");
                    }
                }
                treeNode.getChildren().add(findChildrenForList(it, treeNodes));
            }
        }
        return treeNode;
    }

}




