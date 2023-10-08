package com.star.webssh.common;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/*** 远程调用Linux shell 命令*/
//系统资源监控工具类
public class LinuxStateForShell {
    public static final String CPU_MEM_SHELL = "top -b -n 1";
    public static final String FILES_SHELL = "df -hl";
    public static final String[] COMMANDS = {CPU_MEM_SHELL, FILES_SHELL};
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static Session session;


    /*** 连接到指定的远端主机
     *
     *@returnisConnect
     *@throwsJSchException JSchException*/

    private static boolean connect(String user, String passwd, String host) {//host为IP地址

        JSch jsch = new JSch();
        try {

            session = jsch.getSession(user, host, 22);

            session.setPassword(passwd);

            java.util.Properties config = new java.util.Properties();

            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);

            session.connect();

        } catch (JSchException e) {

            e.printStackTrace();

            System.out.println("connect error !");
            return false;

        }
        return true;

    }

    /*** 远程连接Linux 服务器 执行相关的命令
     *@paramcommands 执行的脚本
     *@paramuser 远程连接的用户名
     *@parampasswd 远程连接的密码
     *@paramhost 远程连接的主机IP
     *@return最终命令返回信息*/

    public static Map runDistanceShell(String[] commands, String user, String passwd, String host) {
        if (!connect(user, passwd, host)) {
            return null;
        }

        Map map = new HashMap<>();

        StringBuilder stringBuffer;

        BufferedReader reader = null;

        Channel channel = null;
        try {
            for (String command : commands) {

                stringBuffer = new StringBuilder();

                channel = session.openChannel("exec");

                ((ChannelExec) channel).setCommand(command);

                channel.setInputStream(null);

                ((ChannelExec) channel).setErrStream(System.err);

                channel.connect();

                InputStream in = channel.getInputStream();

                reader = new BufferedReader(new InputStreamReader(in));

                String buf;
                while ((buf = reader.readLine()) != null) {//舍弃PID 进程信息

                    if (buf.contains("PID")) {
                        break;

                    }

                    stringBuffer.append(buf.trim()).append(LINE_SEPARATOR);

                }//每个命令存储自己返回数据-用于后续对返回数据进行处理

                map.put(command, stringBuffer.toString());

            }

        } catch (IOException | JSchException e) {

            e.printStackTrace();

        } finally {
            try {
                if (reader != null) {

                    reader.close();

                }

            } catch (IOException e) {

                e.printStackTrace();

            }
            if (channel != null) {

                channel.disconnect();

            }

            session.disconnect();

        }
        return map;

    }

    /*** 直接在本地执行 shell
     *
     *@paramcommands 执行的脚本
     *@return执行结果信息*/

    public static Map runLocalShell(String[] commands) {

        Runtime runtime = Runtime.getRuntime();

        Map map = new HashMap<>();

        StringBuilder stringBuffer;

        BufferedReader reader;

        Process process;
        for (String command : commands) {

            stringBuffer = new StringBuilder();
            try {

                process = runtime.exec(command);

                InputStream inputStream = process.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String buf;
                while ((buf = reader.readLine()) != null) {//舍弃PID 进程信息

                    if (buf.contains("PID")) {
                        break;

                    }

                    stringBuffer.append(buf.trim()).append(LINE_SEPARATOR);

                }

            } catch (IOException e) {

                e.printStackTrace();
                return null;

            }//每个命令存储自己返回数据-用于后续对返回数据进行处理

            map.put(command, stringBuffer.toString());

        }
        return map;

    }

    /*** 处理 shell 返回的信息
     *
     * 具体处理过程以服务器返回数据格式为准
     * 不同的Linux 版本返回信息格式不同
     *
     *@paramresult shell 返回的信息
     *@return最终处理后的信息*/

    public static String disposeResultMessage(Map result) {

        StringBuilder buffer = new StringBuilder();
        for (String command : COMMANDS) {

            String commandResult = result.get(command).toString();
            if (commandResult == null) continue;
            if (command.equals(CPU_MEM_SHELL)) {

                String[] strings = commandResult.split(LINE_SEPARATOR);//将返回结果按换行符分割

                for (String line : strings) {

                    line = line.toUpperCase();
                    //转大写处理//处理CPU Cpu(s): 10.8%us, 0.9%sy, 0.0%ni, 87.6%id, 0.7%wa, 0.0%hi, 0.0%si, 0.0%st

                    if (line.startsWith("%CPU(S):")) {

                        String cpuStr = "CPU 用户使用占有率:";
                        try {

                            cpuStr += line.split(":")[1].split(",")[0].replace("US", "");
                            cpuStr +="%";

                        } catch (Exception e) {

                            e.printStackTrace();

                            cpuStr += "计算过程出错";

                        }

                        buffer.append(cpuStr).append(LINE_SEPARATOR);

                        //处理内存 Mem: 66100704k total, 65323404k used, 777300k free, 89940k buffers
                    } else if (line.startsWith("MIB MEM")) {

                        String memStr = "系统物理内存使用情况:";
                        try {

                            memStr += line.split(":")[1]

                                    .replace("TOTAL", "k 总计")

                                    .replace("FREE", "k 空闲")

                                    .replace("USED", "k 已使用")

                                    .replace("BUFF/CACHE", "k 缓存");

                        } catch (Exception e) {

                            e.printStackTrace();

                            memStr += "计算过程出错";

                            buffer.append(memStr).append(LINE_SEPARATOR);
                            continue;

                        }

                        buffer.append(memStr).append(LINE_SEPARATOR);

                    }

                }

            } else if (command.equals(FILES_SHELL)) {//处理系统磁盘状态

                buffer.append("系统磁盘状态:");
                try {

                    buffer.append(disposeFilesSystem(commandResult)).append(LINE_SEPARATOR);

                } catch (Exception e) {

                    e.printStackTrace();

                    buffer.append("计算过程出错").append(LINE_SEPARATOR);

                }

            }

        }
        return buffer.toString();

    }
    //处理系统磁盘状态

    /*** Filesystem Size Used Avail Use% Mounted on
     * /dev/sda3 442G 327G 93G 78% /
     * tmpfs 32G 0 32G 0% /dev/shm
     * /dev/sda1 788M 60M 689M 8% /boot
     * /dev/md0 1.9T 483G 1.4T 26% /ezsonar
     *
     *@paramcommandResult 处理系统磁盘状态shell执行结果
     *@return处理后的结果*/

    private static String disposeFilesSystem(String commandResult) {

        String[] strings = commandResult.split(LINE_SEPARATOR);
        //final String PATTERN_TEMPLATE = "([a-zA-Z0-9%_/]*)\\s";

        int size = 0;
        int used = 0;
        for (int i = 0; i < strings.length - 1; i++) {
            if (i == 0) continue;
            int temp = 0;
            for (String s : strings[i].split("\\b")) {
                if (temp == 0) {

                    temp++;
                    continue;

                }
                if (!s.trim().isEmpty()) {
                    if (temp == 1) {

                        size += disposeUnit(s);

                        temp++;

                    } else {

                        used += disposeUnit(s);

                        temp = 0;

                    }

                }

            }

        }
        return new StringBuilder().append("大小 ").append(size).append("G , 已使用").append(used).append("G ,空闲")

                .append(size - used).append("G").toString();

    }

    /*** 处理单位转换
     * K/KB/M/T 最终转换为G 处理
     *
     *@params 带单位的数据字符串
     *@return以G 为单位处理后的数值*/

    private static int disposeUnit(String s) {
        try {

            s = s.toUpperCase();

            String lastIndex = s.substring(s.length() - 1);

            String num = s.substring(0, s.length() - 1);
            int parseInt = Integer.parseInt(num);
            if (lastIndex.equals("G")) {
                return parseInt;

            } else if (lastIndex.equals("T")) {
                return parseInt * 1024;

            } else if (lastIndex.equals("M")) {
                return parseInt / 1024;

            } else if (lastIndex.equals("K") || lastIndex.equals("KB")) {
                return parseInt / (1024 * 1024);

            }

        } catch (NumberFormatException e) {

            e.printStackTrace();
            return 0;

        }
        return 0;

    }

    public static void main(String[] args) {

        Map result = runDistanceShell(COMMANDS, "root", "Ft8JDaSqZxhWEE8", "116.63.188.39");

        System.out.println(disposeResultMessage(result));

    }


}

