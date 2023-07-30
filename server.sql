create table ssh_server
(
    id            bigint auto_increment comment '主键',
    user_id       bigint                not null comment '用户id',
    ssh_user_name varchar(255)          not null comment '服务器名',
    ssh_host      varchar(255)          not null comment '服务器IP',
    ssh_port      int                   not null comment '端口号',
    ssh_password  varchar(255)          not null comment '服务器密码',
    ssh_name      varchar(255)          not null comment '服务器用户名',
    remark        varchar(255)          null comment '备注',
    ssh_class     varchar(255)          null comment '服务器类型',
    update_time   datetime              not null comment '更新时间',
    create_time   datetime              not null comment '创建时间',
    status     int unsigned zerofill not null comment '默认连接账号，1表示默认连接，0表示不是',
    primary key (id, ssh_user_name)
);
INSERT INTO `ssh_server` (`id`, `user_id`, `ssh_user_name`, `ssh_host`, `ssh_port`, `ssh_password`, `ssh_name`, `remark`, `ssh_class`, `update_time`, `create_time`, `status`) VALUES (00000000000000000001, 1, '张三', '192.168.0.1', 22, '123456', 'admin', '五', 'linux', '2023-07-27 00:04:29', '2023-07-27 00:04:29', 1);
INSERT INTO `ssh_server` (`id`, `user_id`, `ssh_user_name`, `ssh_host`, `ssh_port`, `ssh_password`, `ssh_name`, `remark`, `ssh_class`, `update_time`, `create_time`, `status`) VALUES (01684237748880265217, 1, '比尔盖茨', '192.168.0.1', 22, '123456', '4444', '五', 'linux', '2023-07-27 00:22:07', '2023-07-27 00:22:03', 0);



