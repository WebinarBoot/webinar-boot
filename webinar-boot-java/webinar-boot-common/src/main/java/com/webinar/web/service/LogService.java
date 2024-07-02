package com.webinar.web.service;

import com.webinar.reApi.ApiResult;
import com.webinar.web.param.LoginUserParam;

public interface LogService {

    ApiResult login(LoginUserParam param);

    ApiResult logOut();
}
