package com.webinar.common.core.domain;

import lombok.Data;

@Data
public class AccountParams extends BasicPageParams {

    private String userName;


    private String nickName;


    private String deptId;

}
