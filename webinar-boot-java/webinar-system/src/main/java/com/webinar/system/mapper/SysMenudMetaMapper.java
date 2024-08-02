package com.webinar.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.webinar.common.core.domain.entity.SysMenuDMeta;
import org.apache.ibatis.annotations.Select;

/**
* @author webinar
* @description 针对表【sys_menuD_meta】的数据库操作Mapper
* @createDate 2024-07-31 13:59:08
* @Entity generator.domain.SysMenudMeta
*/
public interface SysMenudMetaMapper extends BaseMapper<SysMenuDMeta> {

    @Select("SELECT * FROM sys_menuD_meta where menu_id=#{menuId}")
    SysMenuDMeta queryMetaByMenuId(Long menuId);
}




