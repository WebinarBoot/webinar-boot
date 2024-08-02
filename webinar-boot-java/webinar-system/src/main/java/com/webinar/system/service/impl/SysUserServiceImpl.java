package com.webinar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.webinar.common.annotation.DataScope;
import com.webinar.common.constant.UserConstants;
import com.webinar.common.core.domain.AccountParams;
import com.webinar.common.core.domain.entity.SysDept;
import com.webinar.common.core.domain.entity.SysRole;
import com.webinar.common.core.domain.entity.SysUser;
import com.webinar.common.exception.ServiceException;
import com.webinar.common.utils.PageUtils;
import com.webinar.common.utils.Query;
import com.webinar.common.utils.SecurityUtils;
import com.webinar.common.utils.StringUtils;
import com.webinar.common.utils.bean.BeanValidators;
import com.webinar.common.utils.spring.SpringUtils;
import com.webinar.system.domain.SysPost;
import com.webinar.system.domain.SysUserPost;
import com.webinar.system.domain.SysUserRole;
import com.webinar.system.mapper.*;
import com.webinar.system.service.ISysConfigService;
import com.webinar.system.service.ISysDeptService;
import com.webinar.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 * 
 * @author webinar
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    protected Validator validator;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     * 
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotEmpty(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    @Override
    public List<SysUser> exportExcel(Map<String, Object> params) {
        List<SysUser> list = new ArrayList<>();
        if (StringUtils.isNotNull(params.get("deptId"))){
            // 当部门id不为空的时候要实现查到部门下所有子部门的所有用户必须用手动分页实现
            list = getResultExport(params);
        }else {
            list = queryExport(params);
        }
        //处理角色 部门等信息
        for (SysUser sysUser : list) {
            SysDept sysDept = sysDeptMapper.selectDeptById(sysUser.getDeptId());
            if(!ObjectUtils.isEmpty(sysDept)){
                sysUser.setDeptName(sysDept.getDeptName());
            }
            SysRole roloById = userRoleMapper.getRoloById(sysUser);
            if(!ObjectUtils.isEmpty(roloById)){
                sysUser.setRoleName(roloById.getRoleName());
            }
        }
        return list;
    }

    @Override
    public PageUtils page(Map<String,Object> params) {
        PageUtils pageResult;
        if (StringUtils.isNotNull(params.get("deptId"))){
            // 当部门id不为空的时候要实现查到部门下所有子部门的所有用户必须用手动分页实现
            pageResult = getResultList(params);
        }else {
            pageResult = queryPage(params);
        }
        List<SysUser> list = (List<SysUser>)pageResult.getList();
        //处理角色 部门等信息
        for (SysUser sysUser : list) {
            SysDept sysDept = sysDeptMapper.selectDeptById(sysUser.getDeptId());
            if(!ObjectUtils.isEmpty(sysDept)){
                sysUser.setDeptName(sysDept.getDeptName());
            }
            SysRole roloById = userRoleMapper.getRoloById(sysUser);
            if(!ObjectUtils.isEmpty(roloById)){
                sysUser.setRoleName(roloById.getRoleName());
            }
        }
        pageResult.setList(list);
        return pageResult;
    }

    private PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        String chineseName = (String) params.get("chineseName");
        IPage<SysUser> page = this.page(
                new Query<SysUser>(params).getPage(),
                new QueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(username), "user_name", username)
                        .like(StringUtils.isNotBlank(chineseName), "nick_name", chineseName)
                        .eq("status",0)
                        .eq("del_flag",0)
        );
        return new PageUtils(page);
    }
    private List<SysUser> queryExport(Map<String, Object> params) {
        String username = (String) params.get("username");
        String chineseName = (String) params.get("chineseName");
        List<SysUser> page = this.list(
                new QueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(username), "user_name", username)
                        .like(StringUtils.isNotBlank(chineseName), "nick_name", chineseName)
                        .eq("status",0)
                        .eq("del_flag",0)
        );
        return page;
    }
    /**
     * 部门用户
     * @param params
     * @return
     */
    private PageUtils getResultList(Map<String, Object> params) {
        String username = (String) params.get("username");
        String chineseName = (String) params.get("chineseName");
        String deptId = (String) params.get("deptId");
        List<String> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        List<String> deptIdss = new ArrayList<>();
        deptIdss.add(deptId);
        //获取所有子部门ID
        List<String> childs= getDeptChilds(deptIds,deptIdss);
        IPage<SysUser> page = this.page(
                new Query<SysUser>(params).getPage(),
                new QueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(username), "user_name", username)
                        .like(StringUtils.isNotBlank(chineseName), "nick_name", chineseName)
                        .in(childs.size()>0,"dept_id",childs)
                        .eq("status",0)
                        .eq("del_flag",0)

        );
        return new PageUtils(page);
    }

    private List<SysUser> getResultExport(Map<String, Object> params) {
        String username = (String) params.get("username");
        String chineseName = (String) params.get("chineseName");
        String deptId = (String) params.get("deptId");
        List<String> deptIds = new ArrayList<>();
        deptIds.add(deptId);
        List<String> deptIdss = new ArrayList<>();
        deptIdss.add(deptId);
        //获取所有子部门ID
        List<String> childs= getDeptChilds(deptIds,deptIdss);
        List<SysUser> page = this.list(
                new QueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(username), "user_name", username)
                        .like(StringUtils.isNotBlank(chineseName), "nick_name", chineseName)
                        .in(childs.size()>0,"dept_id",childs)
                        .eq("status",0)
                        .eq("del_flag",0)

        );
        return page;
    }
    /**
     * 递归查询所有子部门
     * @param deptIds
     * @return
     */
    private List<String> getDeptChilds(List<String> tDeptIds,List<String> deptIds) {
        if(tDeptIds.size()>0){
            for (String deptId : tDeptIds) {
                deptIds.add(deptId);
                List<SysDept> parent_id = sysDeptMapper.selectList(new QueryWrapper<SysDept>().eq("parent_id", deptId));
                if(parent_id.size()>0){
                    List<String> stringList = new ArrayList<>();
                    for (SysDept sysDept : parent_id) {
                        stringList.add(String.valueOf(sysDept.getDeptId()));
                    }
                    return getDeptChilds(stringList,deptIds);
                }
            }
        }
        return deptIds;
    }


    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, user);
                    deptService.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
