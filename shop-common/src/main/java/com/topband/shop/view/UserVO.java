package com.topband.shop.view;

import lombok.Data;

import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserDTO
 * @packageName: com.topband.shop.dto
 * @description: UserVO
 * @date 2022/9/9 14:20
 */
@Data
public class UserVO implements Serializable {
    private Long id;
    private String email;
}
