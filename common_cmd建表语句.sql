create database if not exists ssh_project;

use ssh_project;
CREATE TABLE if not exists `common_cmd`
(
    `id`          varchar(32)                  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(32) COLLATE utf8_bin NOT NULL COMMENT '组名',
    `user_id`     bigint                       NOT NULL COMMENT '用户id',
    `server_id`   bigint                       NOT NULL COMMENT '服务器id',

    `create_time` datetime                      DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                      DEFAULT NULL COMMENT '更新时间',
    `cmds`        varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '指令集',
    `remarks`     varchar(100) COLLATE utf8_bin COMMENT '备注',

    PRIMARY KEY (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin COMMENT ='常用命令';


create table if not exists employee
(
    id          bigint      not null comment '主键'
        primary key,
    name        varchar(32) comment '姓名',
    username    varchar(32) comment '用户名',
    password    varchar(64) not null comment '密码',
    phone       varchar(11) comment '手机号',
    sex         varchar(2) comment '性别',
    id_number   varchar(18) comment '身份证号',
    status      int default 1 comment '状态 0:禁用，1:正常',
    create_time datetime comment '创建时间',
    update_time datetime comment '更新时间',
    create_user bigint comment '创建人',
    update_user bigint comment '修改人',
    constraint idx_username
        unique (username)
)
    comment '员工信息' collate = utf8_bin;

create table if not exists ssh_info
(
    id             bigint(10) unsigned zerofill auto_increment comment 'id'
        primary key,
    user_id        varchar(255) not null comment '用户id',
    user           varchar(255) not null comment '用户',
    operator       varchar(255) null comment '操作',
    url            varchar(255) null comment '访问路径',
    local_address  varchar(255) not null comment '本地地址',
    remote_address varchar(255) not null comment '连接地址',
    login_time     datetime     not null comment '登陆时间',
    operator_time  datetime     not null comment '操作时间'
);



create table if not exists ssh_server
(
    id            bigint auto_increment comment '主键',
    user_id       bigint                not null comment '用户id',
    ssh_user_name varchar(255)          comment '服务器名',
    ssh_host      varchar(255)          not null comment '服务器IP',
    ssh_port      int                   not null comment '端口号',
    ssh_password  varchar(255)          not null comment '服务器密码',
    ssh_name      varchar(255)          not null comment '服务器用户名',
    remark        varchar(255)          null comment '备注',
    ssh_class     varchar(255)          null comment '服务器类型',
    update_time   datetime               comment '更新时间',
    create_time   datetime               comment '创建时间',
    status        int unsigned zerofill  comment '默认连接账号，1表示默认连接，0表示不是',
    primary key (id, ssh_user_name)
);
INSERT INTO `ssh_server` (`id`, `user_id`, `ssh_user_name`, `ssh_host`, `ssh_port`, `ssh_password`, `ssh_name`,
                          `remark`, `ssh_class`, `update_time`, `create_time`, `status`)
VALUES (00000000000000000001, 1, '张三', '192.168.0.1', 22, '123456', 'admin', '五', 'linux', '2023-07-27 00:04:29',
        '2023-07-27 00:04:29', 1);
INSERT INTO `ssh_server` (`id`, `user_id`, `ssh_user_name`, `ssh_host`, `ssh_port`, `ssh_password`, `ssh_name`,
                          `remark`, `ssh_class`, `update_time`, `create_time`, `status`)
VALUES (01684237748880265217, 1, '比尔盖茨', '192.168.0.1', 22, '123456', '4444', '五', 'linux', '2023-07-27 00:22:07',
        '2023-07-27 00:22:03', 0);



