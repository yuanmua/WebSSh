-- 创建数据库
CREATE DATABASE IF NOT EXISTS ssh_project;

-- 切换到目标数据库
USE ssh_project;

-- 创建 common_cmd 表
CREATE TABLE IF NOT EXISTS common_cmd (
                                          id STRING,
                                          name STRING,
                                          user_id BIGINT,
                                          server_id BIGINT,
                                          create_time TIMESTAMP,
                                          update_time TIMESTAMP,
                                          cmds STRING,
                                          remarks STRING
)
    COMMENT '常用命令'
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS TEXTFILE;

-- 创建 employee 表
CREATE TABLE IF NOT EXISTS employee (
                                        id STRING,
                                        name STRING,
                                        username STRING,
                                        password STRING,
                                        phone STRING,
                                        sex STRING,
                                        id_number STRING,
                                        status INT DEFAULT 1,
                                        create_time TIMESTAMP,
                                        update_time TIMESTAMP,
                                        create_user BIGINT,
                                        update_user BIGINT
)
    COMMENT '员工信息'
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS TEXTFILE;

-- 创建 ssh_info 表
CREATE TABLE IF NOT EXISTS ssh_info (
                                        id STRING,
                                        user_id STRING,
                                        `user` STRING,
                                        operator STRING,
                                        url STRING,
                                        local_address STRING,
                                        remote_address STRING,
                                        login_time TIMESTAMP,
                                        operator_time TIMESTAMP
)
    COMMENT 'SSH 连接信息'
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS TEXTFILE;

-- 创建 ssh_server 表
CREATE TABLE IF NOT EXISTS ssh_server (
                                          id STRING,
                                          user_id BIGINT,
                                          ssh_user_name STRING,
                                          ssh_host STRING,
                                          ssh_port INT,
                                          ssh_password STRING,
                                          ssh_name STRING,
                                          remark STRING,
                                          ssh_class STRING,
                                          update_time TIMESTAMP,
                                          create_time TIMESTAMP,
                                          status INT
)
    COMMENT 'SSH 服务器信息'
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\t'
    STORED AS TEXTFILE;

-- 插入数据
INSERT INTO TABLE ssh_server VALUES
                                 (00000000000000000001, 1, '张三', '192.168.0.1', 22, '123456', 'admin', '五', 'linux', '2023-07-27 00:04:29', '2023-07-27 00:04:29', 1),
                                 (01684237748880265217, 1, '比尔盖茨', '192.168.0.1', 22, '123456', '4444', '五', 'linux', '2023-07-27 00:22:07', '2023-07-27 00:22:03', 0);



create external table covid2
(
    date_value date,
    county     string,
    state      string,
    fips       string,
    cases      int,
    deaths     int
);


select * from covid2;