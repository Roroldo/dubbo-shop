package com.topband.shop.provider.mapper;

import com.topband.shop.view.PermissionVO;

import java.util.List;

public interface PermissionMapper {
    List<PermissionVO> list(Long roleId);
}
