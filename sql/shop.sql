create table if not exists hyj_admin
(
    id          bigint            not null comment '管理员 id'
        primary key,
    name        varchar(64)       not null comment '管理员名',
    email       varchar(128)      not null comment '邮箱',
    password    varchar(128)      not null comment '密码',
    type        tinyint default 0 not null comment '管理员类型：0 为 普通管理员，1 为超级管理员',
    status      tinyint default 0 not null comment '用户状态：0 表示正常，1表示冻结',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标记，1 表示删除，0 表示未删除',
    constraint hyj_admin_email_uindex
        unique (email),
    constraint hyj_admin_id_uindex
        unique (id),
    constraint hyj_admin_name_uindex
        unique (name)
)
    comment '管理员表';

create index hyj_admin_create_time_index
    on hyj_admin (create_time);

create index hyj_admin_type_index
    on hyj_admin (type);

create table if not exists hyj_admin_role
(
    id          bigint            not null comment '主键 id'
        primary key,
    admin_id    bigint            not null comment '管理员 id',
    role_id     bigint            not null comment '角色 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 null comment '删除标记，1 表示已删除，0 表示未删除',
    constraint hyj_admin_role_id_uindex
        unique (id),
    constraint hyj_admin_role_role_id_admin_id_uindex
        unique (role_id, admin_id)
)
    comment '成员角色中间表';

create index hyj_admin_role_admin_id_index
    on hyj_admin_role (admin_id);

create index hyj_admin_role_role_id_index
    on hyj_admin_role (role_id);

create table if not exists hyj_goods
(
    id          bigint            not null comment '商品 id'
        primary key,
    category    varchar(64)       null comment '商品品类',
    image_url   varchar(256)      null comment '图片地址',
    name        varchar(64)       not null comment '商品名称',
    goods_price decimal(5, 2)     not null comment '商品价格',
    total       int               not null comment '商品库存',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标记，1 表示删除，0 表示未删除',
    constraint hyj_goods_id_uindex
        unique (id),
    constraint hyj_goods_name_uindex
        unique (name)
)
    comment '商品信息表';

create table if not exists hyj_log
(
    id          bigint            not null comment '日志 id'
        primary key,
    admin_id    bigint            not null comment '操作人',
    content     varchar(256)      not null comment '操作内容',
    create_time datetime          not null comment '创建日期',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标记，1表示删除，0表示未删除',
    constraint hyj_log_id_uindex
        unique (id)
)
    comment 'web 端日志表';

create index hyj_log_admin_id_index
    on hyj_log (admin_id);

create index hyj_log_create_time_index
    on hyj_log (create_time);

create index hyj_log_update_time_index
    on hyj_log (update_time);

create table if not exists hyj_order
(
    id          bigint            not null comment '订单 id'
        primary key,
    user_id     bigint            not null comment '用户 id',
    voucher_id  bigint            null comment '优惠卷 id',
    goods_id    bigint            not null comment '商品 id',
    order_price decimal(10, 2)    not null comment '商品价格',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标记，1 表示删除，0 表示未删除',
    constraint hyj_order_id_uindex
        unique (id)
)
    comment '订单表';

create index hyj_order_create_time_index
    on hyj_order (create_time);

create index hyj_order_goods_id_index
    on hyj_order (goods_id);

create index hyj_order_user_id_index
    on hyj_order (user_id);

create index hyj_order_voucher_id_index
    on hyj_order (voucher_id);

create table if not exists hyj_permission
(
    id          bigint        not null
        primary key,
    url         varchar(256)  null comment '权限路径',
    name        varchar(64)   not null comment '权限名',
    `order`     int           not null comment '相同级别权限的顺序',
    type        tinyint       not null comment '1 为一级权限，2 为二级权限，3 为三级权限',
    parent_id   bigint        not null comment '父亲权限 id',
    update_time datetime      not null comment '更新时间',
    create_time datetime      not null comment '创建时间',
    del_flag    int default 0 not null comment '删除标记，1 表示删除，0表示未删除',
    constraint hyj_permission_id_uindex
        unique (id),
    constraint hyj_permission_url_uindex
        unique (url)
);

create index hyj_permission_order_index
    on hyj_permission (`order`);

create index hyj_permission_parent_id_index
    on hyj_permission (parent_id);

create index hyj_permission_type_index
    on hyj_permission (type);

create table if not exists hyj_role
(
    id              bigint            not null comment '角色 id'
        primary key,
    name            varchar(64)       not null comment '角色名',
    create_time     datetime          not null comment '创建时间',
    update_time     datetime          not null comment '更新时间',
    del_flag        tinyint default 0 not null comment '删除标记，1 表示删除，0 表示未删除',
    create_admin_id bigint            not null comment '创建角色的管理员 id',
    `order`         int     default 0 not null comment '角色顺序',
    constraint hyj_role_id_uindex
        unique (id),
    constraint hyj_role_name_uindex
        unique (name)
)
    comment '角色表';

create index hyj_role_create_admin_id_index
    on hyj_role (create_admin_id);

create index hyj_role_create_time_index
    on hyj_role (create_time);

create index hyj_role_order_index
    on hyj_role (`order`);

create table if not exists hyj_role_permission
(
    id            bigint            not null comment '主键 id',
    role_id       bigint            not null comment '角色 id',
    permission_id bigint            not null comment '权限 id',
    create_time   datetime          not null comment '创建时间',
    update_time   datetime          not null comment '更新时间',
    del_flag      tinyint default 0 not null comment '删除标记，1表示删除，0表示未删除',
    constraint hyj_role_permission_id_uindex
        unique (id),
    constraint hyj_role_permission_role_id_permission_id_del_flag_uindex
        unique (role_id, permission_id, del_flag)
)
    comment '角色与权限中间表';

create index hyj_role_permission_permission_id_index
    on hyj_role_permission (permission_id);

create index hyj_role_permission_role_id_index
    on hyj_role_permission (role_id);

create table if not exists hyj_upload_file
(
    id          bigint            not null
        primary key,
    admin_id    bigint            not null comment '上传管理员 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标记，1表示删除，0表示未删除',
    filename    varchar(256)      not null comment '上传文件名',
    upload_time datetime          not null comment '上传时间',
    status      tinyint           not null comment '文件导入状态：0 为导入中，1 是导入完成',
    constraint hyj_upload_file_filename_uindex
        unique (filename),
    constraint hyj_upload_file_id_uindex
        unique (id)
)
    comment '导入文件记录表';

create index hyj_upload_file_admin_id_index
    on hyj_upload_file (admin_id);

create table if not exists hyj_user
(
    id          bigint               not null comment '用户 id'
        primary key,
    email       varchar(32)          not null comment '用户邮箱',
    password    varchar(128)         not null comment '密码',
    create_time datetime             null comment '创建时间',
    update_time datetime             null comment '更新时间',
    del_flag    tinyint(1) default 0 not null comment '删除标记，1表示删除，0表示未删除',
    constraint hyj_client_user_email_uindex
        unique (email),
    constraint hyj_client_user_id_uindex
        unique (id)
)
    comment 'C 端用户表';

create table if not exists hyj_user_active
(
    id          bigint            not null comment '主键 id'
        primary key,
    user_id     bigint            not null comment 'c 端用户 id',
    active_time date              not null comment '活跃时间',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标志',
    constraint hyj_client_user_active_id_uindex
        unique (id)
)
    comment 'client 用户活跃表';

create table if not exists hyj_user_voucher
(
    id          bigint            not null comment '主键 id'
        primary key,
    user_id     bigint            not null comment '用户 id',
    voucher_id  bigint            not null comment '优惠卷 id',
    create_time datetime          not null comment '创建时间',
    update_time datetime          not null comment '更新时间',
    del_flag    tinyint default 0 not null comment '删除标记，1表示删除，0表示未删除',
    constraint hyj_user_voucher_id_uindex
        unique (id)
)
    comment '用户与优惠卷中间表';

create index hyj_user_voucher_user_id_index
    on hyj_user_voucher (user_id);

create index hyj_user_voucher_voucher_id_index
    on hyj_user_voucher (voucher_id);

create table if not exists hyj_voucher
(
    id            bigint            not null comment '优惠卷 id'
        primary key,
    expire_time   datetime          not null comment '过期时间',
    count         int               not null comment '库存',
    voucher_price decimal(5, 2)     not null comment '优惠价格',
    create_time   datetime          not null comment '创建时间',
    update_time   datetime          not null comment '更新时间',
    del_flag      tinyint default 0 not null comment '删除标记，1表示删除，0表示未删除',
    name          varchar(64)       not null comment '优惠卷名称',
    constraint hyj_voucher_id_uindex
        unique (id),
    constraint hyj_voucher_name_uindex
        unique (name)
)
    comment '优惠卷表';

create index hyj_voucher_create_time_index
    on hyj_voucher (create_time);

