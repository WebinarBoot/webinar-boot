package com.webinar.web.controller.system;

import com.webinar.common.annotation.Log;
import com.webinar.common.core.controller.BaseController;
import com.webinar.common.core.domain.AccountParams;
import com.webinar.common.core.domain.AjaxResult;
import com.webinar.common.core.domain.entity.SysDept;
import com.webinar.common.core.domain.entity.SysRole;
import com.webinar.common.core.domain.entity.SysUser;
import com.webinar.common.core.page.TableDataInfo;
import com.webinar.common.enums.BusinessType;
import com.webinar.common.utils.MapUtils;
import com.webinar.common.utils.PageUtils;
import com.webinar.common.utils.SecurityUtils;
import com.webinar.common.utils.StringUtils;
import com.webinar.common.utils.easyExcel.ExcelUtil;
import com.webinar.system.service.ISysDeptService;
import com.webinar.system.service.ISysPostService;
import com.webinar.system.service.ISysRoleService;
import com.webinar.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 * 
 * @author webinar
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @GetMapping("/getAccountList")
    public AjaxResult getAccountList(AccountParams form) {
        // PromiseResult
        Map<String, Object> PromiseResult = new MapUtils();
        // 构建查询条件
        Map<String, Object> params = new MapUtils();
        params.put("username", form.getUserName());
        params.put("chineseName", form.getNickName());
        params.put("deptId", form.getDeptId());
        if (form.getPage() != null && form.getPageSize() != null){
            params.put("page",form.getPage().toString());
            params.put("limit",form.getPageSize().toString());
        }
        List<SysUser> users ;

        // 查询用户
        PageUtils usersPage = userService.page(params);
        users = (List<SysUser>)usersPage.getList();
        PromiseResult.put("items", users);
        PromiseResult.put("total", usersPage.getTotalCount());
        return success(PromiseResult);
    }


    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:account:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestBody AccountParams form) throws Exception {
        Map<String, Object> params = new MapUtils();
        params.put("username", form.getUserName());
        params.put("chineseName", form.getNickName());
        params.put("deptId", form.getDeptId());
        List<SysUser> list = userService.exportExcel(params);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:account:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user)
    {
        deptService.checkDeptDataScope(user.getDeptId());
        SysRole sysRole= roleService.selectRoleByName(user.getRoleName());
        Long[] longs = new Long[1];
        longs[0]=sysRole.getRoleId();
        user.setRoleIds(longs);
        roleService.checkRoleDataScope(sysRole.getRoleId());
        if (!userService.checkUserNameUnique(user))
        {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
//        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
//        {
//            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
//        }
//        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
//        {
//            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        userService.insertUser(user);
        return success("添加成功!");
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:account:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        deptService.checkDeptDataScope(user.getDeptId());
        SysRole sysRole= roleService.selectRoleByName(user.getRoleName());
        Long[] longs = new Long[1];
        longs[0]=sysRole.getRoleId();
        user.setRoleIds(longs);
        roleService.checkRoleDataScope(sysRole.getRoleId());
        if (!userService.checkUserNameUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
//        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
//        {
//            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
//        }
//        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
//        {
//            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
        user.setUpdateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        userService.updateUser(user);
        return success("修改成功");
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, getUserId()))
        {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept)
    {
        return success(deptService.selectDeptTreeList(dept));
    }
}
