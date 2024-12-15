package com.star.webssh.controller;


import com.star.webssh.common.R;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

/**
 * @description: 通用文件上传类
 * @author: yuanmu
 * @create: 2024-12-08 23:59
 **/
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload-path}")
    private String uploadPath;

    @PostMapping("/upload")//普通上传
    public Object uploadFile(@RequestParam("multipartFile") MultipartFile file,
                             @RequestParam("fileType") String fileType) {
        if (file.isEmpty()) {
            return R.error("文件不能为空！");
        }

        // 根据文件类型决定存储路径
        String basePath = uploadPath + "/uploads/";

        //TODO 上线前可以修改

        try {
            String path = new File(URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(), "UTF-8")).getAbsolutePath();
            basePath = path + "/static/uploads/";
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return R.error("文件上传失败！");
        }


        String subPath;
        switch (fileType) {
            case "cover":
                subPath = "covers/";
                break;
            case "content":
                subPath = "contents/";
                break;
            case "avatar":
                subPath = "avatars/";
                break;
            default:
                return R.error("文件类型无效！");
        }

        String uploadPath = basePath + subPath;

        try {
            // 确保目录存在
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 保存文件
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(uploadPath + fileName);
            file.transferTo(dest);

            // 返回文件 URL
            String fileUrl = subPath + fileName;
            return R.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("文件上传失败！");
        }
    }

    @PostMapping("/upload")
    public Object uploadFile(@RequestParam("multipartFile") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error("文件不能为空！");
        }

        String hdfsBasePath = "/uploads"; // HDFS 存储的根目录
        String localBasePath = uploadPath + "/uploads/"; // 本地存储根目录
        boolean useHDFS = true; // true 表示使用 HDFS，false 表示本地存储（测试阶段可切换）

        try {
            // 文件名生成
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            if (useHDFS) {
                // HDFS 配置
                Configuration conf = new Configuration();
                conf.set("fs.defaultFS", "hdfs://172.17.0.2:9000"); // 替换为实际 HDFS 地址
                FileSystem fs = FileSystem.get(conf);

                // 确保 HDFS 目录存在
                Path hdfsDirPath = new Path(hdfsBasePath);
                if (!fs.exists(hdfsDirPath)) {
                    fs.mkdirs(hdfsDirPath);
                }

                // 上传文件到 HDFS
                Path hdfsFilePath = new Path(hdfsBasePath + "/" + fileName);
                FSDataOutputStream outputStream = fs.create(hdfsFilePath);
                outputStream.write(file.getBytes());
                outputStream.close();

                fs.close();
                return R.success(hdfsFilePath.toString());

            } else {
                // 本地存储逻辑
                try {
                    String path = new File(URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(), "UTF-8")).getAbsolutePath();
                    localBasePath = path + "/static/uploads/";
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return R.error("文件上传失败！");
                }

                try {
                    // 确保目录存在
                    File directory = new File(uploadPath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    // 保存文件
                    File dest = new File(uploadPath + fileName);
                    file.transferTo(dest);

                    // 返回文件 URL
                    return R.success(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return R.error("文件上传失败！");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 分片下载文件
     */
    @GetMapping("/download")
    public Object downloadFile(@RequestParam("filePath") String filePath , String name, HttpServletRequest request, HttpServletResponse response) {
        try {
            long startTime = System.currentTimeMillis();
            if (filePath == null || filePath.equals("undefined")) {
                return R.error("文件不能为空！");
            }

            // 解析文件路径
            String basePath = uploadPath + "/uploads/";

            // TODO: 上线前可以修改
            String decodedPath = URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(), "UTF-8");
            basePath = new File(decodedPath).getAbsolutePath() + "/static/uploads/";

//            Path file = Paths.get(basePath, filePath);
            File file = new File(basePath + filePath);

            // 获取文件长度
            long fileLength = file.length();

            // 解析客户端请求的下载范围
            String rangeHeader = request.getHeader("Range");

            long start = 0;
            long end = fileLength - 1;
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.substring(6).split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                }
            }

            // 设置响应头
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + filePath);
            response.setHeader("Content-Length", String.valueOf(end - start + 1));
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);

            // 读取文件的相应部分并发送给客户端
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.skip(start);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

            // 关闭资源
            bufferedInputStream.close();
            fileInputStream.close();
            outputStream.close();
            System.out.print("下载耗时：");
            System.out.println(System.currentTimeMillis() - startTime);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success();

    }


    @GetMapping("/download2")
    public Object downloadFile2(@RequestParam("filePath") String filePath , String name, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        try{
            long startTime = System.currentTimeMillis();
            if (filePath == null || filePath.equals("undefined")) {
                return R.error("文件不能为空！");
            }

            // 解析文件路径
            String basePath = uploadPath + "/uploads/";

            // TODO: 上线前可以修改
            String decodedPath = URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(), "UTF-8");
            basePath = new File(decodedPath).getAbsolutePath() + "/static/uploads/";

//            Path file = Paths.get(basePath, filePath);
            File file = new File(basePath + filePath);


            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(file);
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            //下载文件：需要设置消息头
            //MIME类型,这里为二进制文件（任意文件类型都可）
            response.addHeader("content-Type","application/octet-stream");
            //attachement为附件，以附件方式获取要下载的文件，filename指定具体文件，包含了文件后缀：abc.txt
            response.addHeader("content-Disposition","attachement;filename="+filePath);


            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                try {
                    outputStream.flush();

                }catch (Exception e){}
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
            System.out.print("耗时：");
            System.out.println(System.currentTimeMillis() - start);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success();
    }

    @GetMapping("/download3")
    public Object downloadFile3(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        if (filePath == null || filePath.equals("undefined")) {
            return R.error("文件路径不能为空！");
        }

        long start = System.currentTimeMillis();
        try {
            // 配置 HDFS
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://172.17.0.2:9000"); // 替换为实际 HDFS 地址
            FileSystem fs = FileSystem.get(conf);

            // 目标文件路径
            Path hdfsFilePath = new Path(filePath);

            // 检查文件是否存在
            if (!fs.exists(hdfsFilePath)) {
                return R.error("文件不存在，路径：" + filePath);
            }

            // 打开 HDFS 文件输入流
            FSDataInputStream inputStream = fs.open(hdfsFilePath);

            // 设置响应头，准备下载文件
            response.addHeader("content-Type", "application/octet-stream");
            response.addHeader("content-Disposition", "attachment;filename=" + hdfsFilePath.getName());

            // 输出流，将文件数据写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            // 刷新并关闭流
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            long end = System.currentTimeMillis();
            System.out.println("文件下载完成，耗时：" + (end - start) + "ms");

            return R.success("文件下载成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("文件下载失败：" + e.getMessage());
        }
    }




}


