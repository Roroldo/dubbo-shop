package com.topband.shop.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.AdminRoleService;
import com.topband.shop.api.AdminService;
import com.topband.shop.api.PermissionService;
import com.topband.shop.api.RoleService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.*;
import com.topband.shop.entity.Admin;
import com.topband.shop.entity.AdminRole;
import com.topband.shop.entity.Role;
import com.topband.shop.provider.mapper.AdminMapper;
import com.topband.shop.utils.CheckCodeUtils;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.utils.PasswordEncoderUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.AdminVO;
import com.topband.shop.view.PermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.BusinessConstants.*;
import static com.topband.shop.constants.RedisConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: AdminServiceImpl
 * @date 2022/9/12 17:32
 */
@DubboService
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private AdminRoleService adminRoleService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Override
    public Result login(AdminLoginDTO adminLoginDTO, String checkCodeCookieValue) {
        // 校验验证码
        String checkCodeKey = WEB_LOGIN_CHECK_CODE_KEY + checkCodeCookieValue;
        String checkCode = (String) redisTemplate.opsForValue().get(checkCodeKey);
        // 删除 redis 存储的验证码
        redisTemplate.delete(checkCodeKey);
        // 验证码过期
        if (checkCode == null) {
            return Result.fail(ResultCodeEnum.LOGIN_CHECK_CODE_EXPIRED);
        }
        // 验证码错误
        if (!CheckCodeUtils.isMatch(adminLoginDTO.getCheckCode(), checkCode)) {
            return Result.fail(ResultCodeEnum.LOGIN_CHECK_CODE_ERROR);
        }
        // 根据账号查找用户信息
        Admin admin = adminMapper.selectByEmail(adminLoginDTO.getEmail());
        if (ObjectUtil.isNull(admin)) {
            return Result.fail(ResultCodeEnum.LOGIN_FAIL);
        }
        // 判断账号是否被冻结
        if (admin.getStatus() == ADMIN_LOCKED) {
            return Result.fail(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 校验密码
        if (!PasswordEncoderUtils.matches(admin.getPassword(), adminLoginDTO.getPassword())) {
            return Result.fail(ResultCodeEnum.LOGIN_FAIL);
        }
        // 密码正确
        String loginKey = WEB_LOGIN_KEY + admin.getId();
        String oldToken = (String) redisTemplate.opsForValue().get(loginKey);
        if (oldToken != null) {
            // 删除以前的 token（踢人下线）
            redisTemplate.delete(WEB_TOKEN_KEY + oldToken);
        }

        String token = UUID.randomUUID().toString(true);
        AdminVO adminVo = BeanUtil.copyProperties(admin, AdminVO.class, "status");
        adminVo.setStatus(admin.getStatus() == ADMIN_LOCKED ? LOCKED : NORMAL);
        // 根据用户 id 查找角色名
        Long adminId = admin.getId();
        Role role = adminRoleService.selectByAdminId(adminId);
        if (role == null) {
            return Result.fail();
        }
        String roleName = role.getName();
        adminVo.setRoleName(roleName);

        // 存 token，换成 map 接口
        Map<String, Object> adminVOMap = BeanUtil.beanToMap(adminVo);
        String adminTokenKey = WEB_TOKEN_KEY + token;
        redisTemplate.opsForHash().putAll(adminTokenKey, adminVOMap);
        redisTemplate.expire(adminTokenKey, TOKEN_EXPIRE, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(loginKey, token, TOKEN_EXPIRE, TimeUnit.HOURS);

        Long roleId = role.getId();
        List<PermissionVO> roleMenuTree = permissionService.menuTree(roleId);
        // 返回 token 和权限树列表
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("token", token);
        resultMap.put("adminVO", adminVo);
        resultMap.put("menuTree", roleMenuTree);
        return Result.ok(resultMap);
    }

    @Override
    public Result exit(String token) {
        AdminVO adminVO = getAdminVO(token);
        // 删除登录凭证
        redisTemplate.delete(WEB_TOKEN_KEY + token);
        // 删除踢人下线的副本
        redisTemplate.delete(WEB_LOGIN_KEY + adminVO.getId());
        return Result.ok();
    }

    @Override
    public Result verifyEmail(AdminVerifyDTO adminVerifyDTO) {
        String email = adminVerifyDTO.getEmail();
        String realCheckCode = (String) redisTemplate.opsForValue().get(EMAIL_CHECK_CODE_KEY + email);
        if (realCheckCode == null) {
            return Result.fail(ResultCodeEnum.LOGIN_CHECK_CODE_EXPIRED);
        }
        if (!CheckCodeUtils.isMatch(adminVerifyDTO.getCheckCode(), realCheckCode)) {
            return Result.fail(ResultCodeEnum.LOGIN_CHECK_CODE_ERROR);
        }
        redisTemplate.delete(EMAIL_CHECK_CODE_KEY + email);
        return Result.ok();
    }

    @Override
    public Result resetPasswordByEmailAndCheckCode(AdminForgetPasswordDTO adminForgetPasswordDTO) {
        AdminVerifyDTO adminVerifyDTO = new AdminVerifyDTO(adminForgetPasswordDTO.getEmail(), adminForgetPasswordDTO.getCheckCode());
        // 再次校验验证码
        Result result = this.verifyEmail(adminVerifyDTO);
        if (result.getCode() != 0) {
            return result;
        }
        // 加密新密码，入库
        return updatePassword(adminForgetPasswordDTO.getNewPassword(), adminForgetPasswordDTO.getEmail());
    }

    @Override
    public boolean isFreeze(String email) {
        Admin admin = adminMapper.selectByEmail(email);
        if (admin == null) {
            return false;
        }
        return admin.getStatus() == ADMIN_LOCKED;
    }

    @Override
    public Result updatePasswordById(AdminUpdatePasswordDTO adminUpdatePasswordDTO) {
        Admin admin = adminMapper.selectById(adminUpdatePasswordDTO.getAdminId());
        // 校验旧密码
        if (!PasswordEncoderUtils.matches(admin.getPassword(), adminUpdatePasswordDTO.getOldPassword())) {
            return Result.fail(ResultCodeEnum.PASSWORD_ERROR);
        }
        // 校验两次输入的新密码
        if (!StrUtil.equals(adminUpdatePasswordDTO.getNewPassword(), adminUpdatePasswordDTO.getConfirmNewPassword())) {
            return Result.fail(ResultCodeEnum.PASSWORD_NOT_EQUAL);
        }
        return updatePassword(adminUpdatePasswordDTO.getNewPassword(), admin.getEmail());
    }

    @Override
    public Result updateAdminNameByAdminId(AdminVO adminVO) {
        Admin admin = BeanUtil.copyProperties(adminVO, Admin.class);
        // 判断用户名是否已经被使用
        if (adminMapper.countNotRepeatNameOrEmailById(admin) > 0) {
            return Result.fail(ResultCodeEnum.ADMIN_NAME_REPEAT_ERROR);
        }
        // 先更新数据库
        admin.setUpdateTime(new Date());
        if (adminMapper.updateNameOrEmailById(admin)) {
            String token = (String) redisTemplate.opsForValue().get(WEB_LOGIN_KEY + adminVO.getId());
            // 再更新缓存
            redisTemplate.opsForHash().put(WEB_TOKEN_KEY + token, "name", adminVO.getName());
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result updateEmailByPasswordAndCheckCode(AdminVerifyDTO adminVerifyDTO) {
        // 校验密码
        Long adminId = adminVerifyDTO.getId();
        Admin admin = adminMapper.selectById(adminId);
        if (!PasswordEncoderUtils.matches(admin.getPassword(), adminVerifyDTO.getPassword())) {
            return Result.fail(ResultCodeEnum.PASSWORD_ERROR);
        }
        // 校验邮箱验证码
        Result result = this.verifyEmail(adminVerifyDTO);
        if (result.getCode() != 0) {
            return result;
        }
        admin.setEmail(adminVerifyDTO.getEmail());
        admin.setUpdateTime(new Date());
        if (adminMapper.updateNameOrEmailById(admin)) {
            String token = (String) redisTemplate.opsForValue().get(WEB_LOGIN_KEY + adminId);
            redisTemplate.opsForHash().put(WEB_TOKEN_KEY + token, "email", adminVerifyDTO.getEmail());
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public ResultPage list(AdminQueryDTO adminQueryDTO) {
        int currentPage = CheckPageUtils.getCurrentPage(adminQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(adminQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<AdminVO> adminVOList = adminMapper.list(adminQueryDTO);
        PageInfo<AdminVO> pageInfo = new PageInfo<>(adminVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(AdminLoginDTO adminLoginDTO) {
        // 校验邮箱或用户名是否被使用
        if (adminMapper.countNotRepeatName(adminLoginDTO.getName()) > 0) {
            return Result.fail(ResultCodeEnum.ADMIN_NAME_REPEAT_ERROR);
        }
        if (adminMapper.countNotRepeatEmail(adminLoginDTO.getEmail()) > 0) {
            return Result.fail(ResultCodeEnum.REGISTER_EMAIL_ERROR);
        }

        Admin admin = BeanUtil.copyProperties(adminLoginDTO, Admin.class);
        admin.setPassword(PasswordEncoderUtils.encode(admin.getPassword()));
        admin.setId(SnowFlakeUtils.nextId());
        admin.setCreateTime(new Date());
        admin.setUpdateTime(new Date());
        // 添加角色 id 不能是超级管理员
        Long roleId = adminLoginDTO.getRoleId();
        Role role = roleService.selectById(roleId);
        if (role == null || role.getName().equals(ROOT_ROLE)) {
            return Result.fail(ResultCodeEnum.ROLE_NOT_EXIST);
        }

        AdminRole adminRole = new AdminRole(SnowFlakeUtils.nextId(), admin.getId(), roleId);
        adminRole.setCreateTime(new Date());
        adminRole.setUpdateTime(new Date());
        adminMapper.save(admin);
        adminRoleService.save(adminRole);
        return Result.ok();
    }

    @Override
    public Result deleteById(Long adminId) {
        AdminVO adminVO = selectById(adminId);
        if (adminVO == null || ROOT_ROLE.equals(adminVO.getRoleName())) {
            return Result.fail();
        }
        adminMapper.deleteById(adminId, new Date());
        // 强制下线
        String loginKey = WEB_LOGIN_KEY + adminId;
        String token = (String) redisTemplate.opsForValue().get(loginKey);
        if (token != null) {
            // 删除以前的 token（踢人下线）
            redisTemplate.delete(WEB_TOKEN_KEY + token);
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateById(AdminEditDTO adminEditDTO) {
        Long adminId = adminEditDTO.getAdminId();
        // 修改超管资料
        if (ROOT_ROLE.equals(selectById(adminId).getRoleName())) {
            return Result.fail();
        }
        adminEditDTO.setUpdateTime(new Date());
        // 修改管理员状态
        adminMapper.updateStatusById(adminEditDTO);
        // 添加角色 id 不能是超级管理员
        Long roleId = adminEditDTO.getRoleId();
        Role role = roleService.selectById(roleId);
        if (role == null || role.getName().equals(ROOT_ROLE)) {
            return Result.fail(ResultCodeEnum.ROLE_NOT_EXIST);
        }
        // 修改角色
        adminRoleService.updateByUserId(adminEditDTO);
        // 获取被修改用户的 token
        String token = (String) redisTemplate.opsForValue().get(WEB_LOGIN_KEY + adminId);
        // 如果是在线用户，并且是锁定状态，下线处理
        if (StrUtil.isNotBlank(token)) {
            if (adminEditDTO.getStatus() == ADMIN_LOCKED) {
                // 删除登录凭证
                redisTemplate.delete(WEB_TOKEN_KEY + token);
                redisTemplate.delete(WEB_LOGIN_KEY + adminId);
            } else {
                role = adminRoleService.selectByAdminId(adminId);
                // 更新缓存的管理员信息
                redisTemplate.opsForHash().put(WEB_TOKEN_KEY + token, "roleName", role.getName());
            }
        }
        return Result.ok();
    }

    @Override
    public AdminVO selectById(Long adminId) {
        Admin admin = adminMapper.selectById(adminId);
        if (admin == null) {
            return null;
        }
        AdminVO adminVO = BeanUtil.copyProperties(admin, AdminVO.class, "status");
        adminVO.setStatus(admin.getStatus() == ADMIN_LOCKED ? LOCKED : NORMAL);
        Role role = adminRoleService.selectByAdminId(adminId);
        if (role != null) {
            adminVO.setRoleName(role.getName());
        }
        return adminVO;
    }

    @Override
    public boolean existByEmail(String email) {
        return adminMapper.selectByEmail(email) != null;
    }

    @Override
    public boolean isEmailUsedByOther(Admin admin) {
        // 校验邮箱是否被使用
        return adminMapper.countNotRepeatNameOrEmailById(admin) > 0;
    }

    private AdminVO getAdminVO(String token) {
        String adminTokenKey = WEB_TOKEN_KEY + token;
        Map<Object, Object> adminVOMap = redisTemplate.opsForHash().entries(adminTokenKey);
        AdminVO adminVO = BeanUtil.fillBeanWithMap(adminVOMap, new AdminVO(), false);
        return adminVO;
    }

    private Result updatePassword(String newPassword, String email) {
        newPassword = PasswordEncoderUtils.encode(newPassword);
        if (adminMapper.updatePasswordByEmail(email, newPassword, new Date())) {
            return Result.ok();
        }
        return Result.fail();
    }
}