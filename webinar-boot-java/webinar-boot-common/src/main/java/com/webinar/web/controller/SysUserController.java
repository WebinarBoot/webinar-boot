package com.webinar.web.controller;

import com.webinar.reApi.ApiResult;
import com.webinar.web.param.LoginUserParam;
import com.webinar.web.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private LogService logService;


    /**
     * 自定义登录
     * @param param 登录传参
     * @return
     */
    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginUserParam param) {

        return logService.login(param);

    }


    /**
     * 自定义登出
     * @return
     */
    @PostMapping("/logOut")
    public ApiResult logOut() {

        return logService.logOut();

    }





}