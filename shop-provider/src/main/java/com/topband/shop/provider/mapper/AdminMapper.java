package com.topband.shop.provider.mapper;

import com.topband.shop.dto.AdminEditDTO;
import com.topband.shop.dto.AdminQueryDTO;
import com.topband.shop.entity.Admin;
import com.topband.shop.view.AdminVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: AdminMapper
 * @date 2022/9/12 17:45
 */
public interface AdminMapper {
    Admin selectByEmail(String email);

    boolean updatePasswordByEmail(@Param("email") String email,
                                  @Param("newPassword") String newPassword,
                                  @Param("updateTime") Date updateTime);

    Admin selectById(Long adminId);

    int countNotRepeatNameOrEmailById(Admin admin);

    int countNotRepeatName(String name);

    int countNotRepeatEmail(String email);

    boolean updateNameOrEmailById(Admin admin);

    List<AdminVO> list(AdminQueryDTO adminQueryDTO);

    void save(Admin admin);

    void deleteById(@Param("adminId") Long adminId, @Param("updateTime") Date updateTime);

    void updateStatusById(AdminEditDTO adminEditDTO);
}
