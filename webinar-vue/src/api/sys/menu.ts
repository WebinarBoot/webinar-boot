import { defHttp } from '@/utils/http/axios';
import { getMenuListResultModel } from './model/menuModel';

enum Api {
  GetMenuListById = '/system/menu/getMenuListById',
  GetMenuList = '/system/menu/getMenuList',
}

/**
 * @description: Get user menu based on id
 */

export const getMenuList = () => {
  return defHttp.get<getMenuListResultModel>({ url: Api.GetMenuList });
};
/**
 * @description: 根据 id 获取用户菜单
 */
export const getMenuListById = (params) => {
  return defHttp.get({ url: Api.GetMenuListById, params });
};
