import {
  AccountParams,
  DeptListItem,
  MenuParams,
  RoleParams,
  RolePageParams,
  MenuListGetResultModel,
  DeptListGetResultModel,
  AccountListGetResultModel,
  RolePageListGetResultModel,
  RoleListGetResultModel,
} from './model/systemModel';
import { defHttp } from '@/utils/http/axios';

enum Api {
  AccountList = '/system/user/getAccountList',
  IsAccountExist = '/system/accountExist',
  DeptList = '/system/dept/getDeptTreeList',
  setRoleStatus = '/system/setRoleStatus',
  MenuList = '/system/menu/getMenuListByParams',
  getMenuTopList='/system/menu/getMenuTopList',
  RolePageList = '/system/getRoleListByPage',
  GetAllRoleList = '/system/role/getAllRoleList',
  UpdateMenu = '/system/menu/updateMenu',
  SaveMenu = '/system/menu/saveMenu',
}

export const getAccountList = (params: AccountParams) =>
  defHttp.get<AccountListGetResultModel>({ url: Api.AccountList, params });

export const getDeptList = (params?: DeptListItem) =>
  defHttp.get<DeptListGetResultModel>({ url: Api.DeptList, params });
export const updateMenu = (params) => defHttp.post({ url: Api.UpdateMenu, params },{ successMessageMode: 'message',errorMessageMode: 'message' });
export const saveMenu = (params) => defHttp.post({ url: Api.SaveMenu, params },{ successMessageMode: 'message',errorMessageMode: 'message' });

export const getMenuList = (params?: MenuParams) =>
  defHttp.get<MenuListGetResultModel>({ url: Api.MenuList, params });
export const getMenuTopList = (params?: MenuParams) =>
  defHttp.get<MenuListGetResultModel>({ url: Api.getMenuTopList, params });

export const getRoleListByPage = (params?: RolePageParams) =>
  defHttp.get<RolePageListGetResultModel>({ url: Api.RolePageList, params });

export const getAllRoleList = (params?: RoleParams) =>
  defHttp.get<RoleListGetResultModel>({ url: Api.GetAllRoleList, params });

export const setRoleStatus = (id: number, status: string) =>
  defHttp.post({ url: Api.setRoleStatus, params: { id, status } });

export const isAccountExist = (account: string) =>
  defHttp.post({ url: Api.IsAccountExist, params: { account } }, { errorMessageMode: 'none' });
