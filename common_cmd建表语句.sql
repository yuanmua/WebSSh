use ssh;
CREATE TABLE `common_cmd` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '组名',
   `user_id` bigint NOT NULL COMMENT '用户id',
   `server_id` bigint NOT NULL COMMENT '服务器id',

  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `cmds` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '指令集',
  `remarks` varchar(100) COLLATE utf8_bin  COMMENT '备注',

  PRIMARY KEY (`id`)

) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='常用命令';