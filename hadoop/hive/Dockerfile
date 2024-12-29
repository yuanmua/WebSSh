# 使用官方的 Hive 镜像作为基础镜像
FROM apache/hive:4.0.1


# 切换为 root 用户执行 apt-get 命令
USER root

# 更新包管理器并安装 MySQL 客户端
#RUN apt-get update && apt-get install -y default-mysql-client

# 设置环境变量
ENV HIVE_HOME=/opt/hive
ENV PATH=$HIVE_HOME/bin:$PATH

## 配置 MySQL 数据库
#ENV MYSQL_HOST=hive-mysql
#ENV MYSQL_PORT=3306
#ENV MYSQL_USER=hive
#ENV MYSQL_PASSWORD=hivepassword
#ENV MYSQL_DATABASE=metastore

# 复制本地的配置文件到容器中
COPY hive-site.xml $HIVE_HOME/conf/

# 安装 MySQL JDBC 驱动
#COPY mysql-connector-java-8.0.16.jar $HIVE_HOME/lib/

# 设置 Hive Metastore 数据库连接
#ENV HIVE_METASTORE_DB_TYPE=mysql
#ENV HIVE_METASTORE_URI=jdbc:mysql://<mysql_host>:3306/hive
#ENV HIVE_METASTORE_USER=root
#ENV HIVE_METASTORE_PASSWORD=<password>

ENV DB_DRIVER=postgres
ENV SERVICE_NAME=hiveserver2
# 使用默认的 0.0.0.0 监听所有 IP
EXPOSE 10000

# 启动 Hive 服务
#CMD ["bin/hive", "--service", "metastore"]


#  -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=metastore -e MYSQL_USER=hive -e MYSQL_PASSWORD=hivepassword mysql:5.7
# docker run -it --name hive-server --link hive-mysql:mysql -p 10000:10000 apache/hive:4.0.1 /bin/bash
#docker run -d -p 10000:10000 -p 10002:10002 --env SERVICE_NAME=hiveserver2 --name hive4 apache/hive:4.0.1
#
#
#
#docker run -d -p 9083:9083 --env SERVICE_NAME=metastore --env DB_DRIVER=mysql --env SERVICE_OPTS="-Djavax.jdo.option.ConnectionDriverName=com.mysql.cj.jdbc.Driver -Djavax.jdo.option.ConnectionURL=jdbc:mysql://localhost:33006/metastore?useSSL=false -Djavax.jdo.option.ConnectionUserName=hive -Djavax.jdo.option.ConnectionPassword=hivepassword" --mount source=warehouse,target=/opt/hive/data/warehouse --mount type=bind,source=E:\软件工程\webssh\WebSSh\hadoop\hive\mysql-connector-java-8.0.16.jar,target=/opt/hive/lib/mysql-connector-java.jar --name metastore-standalone apache/hive:4.0.1
#
#docker run -d -p 9083:9083 --env SERVICE_NAME=metastore --env DB_DRIVER=postgres --env SERVICE_OPTS="-Djavax.jdo.option.ConnectionDriverName=org.postgresql.Driver -Djavax.jdo.option.ConnectionURL=jdbc:postgresql://postgres:5432/metastore_db -Djavax.jdo.option.ConnectionUserName=hive -Djavax.jdo.option.ConnectionPassword=password" --mount source=warehouse,target=/opt/hive/data/warehouse --name metastore-standalone apache/hive:${HIVE_VERSION}
#
#
#docker run -d -p 9083:9083
#--env SERVICE_NAME=metastore
#--env DB_DRIVER=mysql
#--env SERVICE_OPTS="-Djavax.jdo.option.ConnectionDriverName=com.mysql.cj.jdbc.Driver -Djavax.jdo.option.ConnectionURL=jdbc:mysql://localhost:33006/metastore?useSSL=false -Djavax.jdo.option.ConnectionUserName=hive -Djavax.jdo.option.ConnectionPassword=hivepassword"
#--mount source=warehouse,target=/opt/hive/data/warehouse
#--mount type=bind,source=E:\软件工程\webssh\WebSSh\hadoop\hive\mysql-connector-java-8.0.16.jar,target=/opt/hive/lib/mysql-connector-java.jar \
#--name metastore-standalone apache/hive:4.0.1
