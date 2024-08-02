package com.webinar.web.controller.system;

import com.webinar.common.constant.Constants;
import com.webinar.common.core.domain.AjaxResult;
import com.webinar.common.core.domain.entity.SysUser;
import com.webinar.common.core.domain.model.LoginBody;
import com.webinar.common.utils.SecurityUtils;
import com.webinar.framework.web.service.SysLoginService;
import com.webinar.framework.web.service.SysPermissionService;
import com.webinar.system.service.ISysUserService;
import com.webinar.system.service.SysMenudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author webinar
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> result = new HashMap<>();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        result.put(Constants.TOKEN, token);
        ajax.put("data", result);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getUserInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> result = new HashMap<>();
        result.put("userId", user.getUserId());
        result.put("username", user.getUserName());
        result.put("realName", user.getNickName());
        result.put("roles", roles);
        result.put("permissions", permissions);
        ajax.put("data",result);
        return ajax;
    }

}
