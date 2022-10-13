package com.topband.shop.base;

import lombok.Getter;

/**
 * @version: V1.0
 * @author: huangyijun
 * @className: ResultCodeEnum
 * @packageName: com.topband.shop.base
 * @description: 状态码和响应消息枚举类
 * @date: 2022-09-08 20:28
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(0, "操作成功"),
    FAIL(-1, "操作失败"),
    PARAM_ERROR(-1, "参数不正确"),
    SERVICE_ERROR(500, "服务异常"),
    LOGIN_DISABLED_ERROR(-1, "该用户已被冻结"),
    REGISTER_EMAIL_ERROR(-1, "邮箱已被使用"),
    ILLEGAL_CALLBACK_REQUEST_ERROR(217, "非法回调请求"),
    FETCH_ACCESS_TOKEN_FAIL(218, "获取accessToken失败"),
    FETCH_USERINFO_ERROR(219, "获取用户信息失败"),
    EMAIL_PATTERN_ERROR(-1, "请输入正确的账号（邮箱格式）"),
    EMAIL_NOT_EXISTS(-1, "此邮箱不是平台用户"),
    EMAIL_SEND_SELF(-1, "修改的邮箱不能和现在一样"),
    PASSWORD_PATTERN_ERROR(-1, "密码由8-20位的数字，大小写字母、特殊符号3种以上组成"),
    DAY_ERROR(-1, "请输入正确的天数"),
    LOGIN_FAIL(-1, "账号或密码错误"),
    LOGIN_CHECK_CODE_ERROR(-1, "验证码错误"),
    VOUCHER_ADD_ERROR(-1, "优惠卷名称已存在"),
    VOUCHER_EXPIRE_ERROR(-1, "优惠卷已经过期"),
    VOUCHER_COUNT_ERROR(-1, "优惠卷库存不足"),
    VOUCHER_SPIKE_REPEAT_ERROR(-1, "只可领去一个未过期的优惠券"),
    VOUCHER_EXIST_NOT_EXPIRED_ERROR(-1, "你还有未过期的优惠卷没有使用"),
    GOODS_TOTAL_ERROR(-1, "商品库存不足"),
    ADMIN_NAME_REPEAT_ERROR(-1, "用户名重复"),
    ORDERS_VOUCHER_REPEAT_ERROR(-1, "您已经使用过该优惠卷"),
    ORDERS_REPEAT_ERROR(-1, "同一笔订单被重复创建"),
    NOT_LOGIN_ERROR(-1, "需要登录"),
    PASSWORD_NOT_EQUAL(-1, "两次输入的密码不一致"),
    PASSWORD_ERROR(-1, "密码错误"),
    EMAIL_SUCCESS_SEND(1, "邮件发送成功"),
    FILE_DOWNLOAD_ERROR(-1, "下载文件失败"),
    LIMIT_MAX_ERROR(-1, "访问过于频繁，请稍后重试"),
    IMAGE_PATTERN_ERROR(-1, "图片格式错误"),
    IMAGE_MAX_SIZE_ERROR(-1, "图片超出上传限制 10MB"),
    GOODS_NAME_REPEAT_ERROR(-1, "商品名称重复"),
    GOODS_DELETE_ERROR(-1, "商品删除失败，已有订单关联"),
    EXCEL_PATTERN_ERROR(-1, "excel 格式错误"),
    EXCEL_MAX_SIZE_ERROR(-1, "excel 大小超出限制 100mb"),
    EXCEL_HEAD_ERROR(-1, "请导入正确模板文件"),
    LOGIN_CHECK_CODE_EXPIRED(-1, "验证码已过期，请重新获取"),
    PERMISSION_REJECT(-1, "权限不足"),
    ROLE_NOT_EXIST(-1, "角色不存在"),
    ROLE_NAME_REPEAT_ERROR(-1, "角色名重复"),
    ROLE_HAS_ADMIN(-1, "角色已和用户关联")
    ;

    /**
     * 业务状态码
     */
    private int code;
    /**
     * 业务消息
     */
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
