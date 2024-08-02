package com.webinar.common.core.domain;

import lombok.Data;

@Data
public class MenuParams extends BasicPageParams {
    private String menuName;
    private String status;
}