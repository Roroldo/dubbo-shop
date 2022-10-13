package com.topband.shop.api;

import com.topband.shop.base.Result;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.*;
import com.topband.shop.entity.Admin;
import com.topband.shop.view.AdminVO;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminService
 * @packageName: com.topband.shop.api
 * @description: AdminService
 * @date 2022/9/12 17:04
 */
public interface AdminService {
    Result login(AdminLoginDTO adminLoginDTO, String checkCodeCookieValue);

    Result exit(String token);

    Result verifyEmail(AdminVerifyDTO adminVerifyDTO);

    Result resetPasswordByEmailAndCheckCode(AdminForgetPasswordDTO adminForgetPasswordDTO);

    boolean isFreeze(String email);

    Result updatePasswordById(AdminUpdatePasswordDTO adminUpdatePasswordDTO);

    Result updateAdminNameByAdminId(AdminVO adminVO);

    Result updateEmailByPasswordAndCheckCode(AdminVerifyDTO adminVerifyDTO);

    ResultPage list(AdminQueryDTO adminQueryDTO);

    Result add(AdminLoginDTO adminLoginDTO);

    Result deleteById(Long adminId);

    Result updateById(AdminEditDTO adminEditDTO);

    AdminVO selectById(Long adminId);

    boolean existByEmail(String email);

    boolean isEmailUsedByOther(Admin admin);
}
