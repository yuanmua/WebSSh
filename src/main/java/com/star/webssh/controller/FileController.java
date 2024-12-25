package com.star.webssh.controller;

import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import io.jsonwebtoken.Claims;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @description: 基于 HDFS 的文件管理控制器
 * @author: yuanmu
 * @create: 2024-12-26
 **/
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${hdfs.uri}")
    private String hdfsUri;

    @Value("${hdfs.user}")
    private String hdfsUser;

    private final HttpServletRequest httpServletRequest;

    public FileController(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    private FileSystem getFileSystem() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);
        return FileSystem.get(conf);
    }

    private Long getUserId() {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new RuntimeException("NOT_LOGIN");
        }

        String jwt = Arrays.stream(cookies)
                .filter(cookie -> "Admin-Token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NOT_LOGIN"));

        Claims claims = JWTUtils.parseJWT(jwt);
        return Long.valueOf(String.valueOf(claims.get("id")));
    }

    @GetMapping("/list")
    public R listFiles(@RequestParam(required = false) String path) {
        try (FileSystem fs = getFileSystem()) {
            Long userId = getUserId();
            String userPath = "/users/" + userId;
            Path dirPath = new Path(path == null ? userPath : userPath + "/" + path);

            if (!fs.exists(dirPath)) {
                return R.error("目录不存在");
            }

            FileStatus[] statuses = fs.listStatus(dirPath);
            List<Map<String, Object>> files = new ArrayList<>();

            for (FileStatus status : statuses) {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("name", status.getPath().getName());
                fileInfo.put("isDirectory", status.isDirectory());
                fileInfo.put("size", status.getLen());
                fileInfo.put("lastModified", status.getModificationTime());
                files.add(fileInfo);
            }

            return R.success(files);
        } catch (IOException e) {
            return R.error("获取文件列表失败：" + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public R uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String path) {
        try (FileSystem fs = getFileSystem()) {
            Long userId = getUserId();
            String userPath = "/users/" + userId;
            Path dirPath = new Path(path == null ? userPath : userPath + "/" + path);
            Path filePath = new Path(dirPath, file.getOriginalFilename());

            if (fs.exists(filePath)) {
                return R.error("文件已存在");
            }

            try (FSDataOutputStream out = fs.create(filePath)) {
                out.write(file.getBytes());
            }

            return R.success("文件上传成功");
        } catch (IOException e) {
            return R.error("文件上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam String path, HttpServletResponse response) {
        try (FileSystem fs = getFileSystem()) {
            Long userId = getUserId();
            String userPath = "/users/" + userId;
            Path filePath = new Path(userPath + "/" + path);

            if (!fs.exists(filePath)) {
                throw new IOException("文件不存在");
            }

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + filePath.getName());

            try (FSDataInputStream in = fs.open(filePath)) {
                org.apache.commons.io.IOUtils.copy(in, response.getOutputStream());
            }
        } catch (IOException e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "下载失败：" + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @DeleteMapping("/delete")
    public R deleteFile(@RequestParam String path) {
        try (FileSystem fs = getFileSystem()) {
            Long userId = getUserId();
            String userPath = "/users/" + userId;
            Path filePath = new Path(userPath + "/" + path);

            if (!fs.exists(filePath)) {
                return R.error("文件不存在");
            }

            fs.delete(filePath, true);
            return R.success("删除成功");
        } catch (IOException e) {
            return R.error("删除失败：" + e.getMessage());
        }
    }


    @PostMapping("/move")
    public R move(@RequestBody Map<String, String> params) {
        try (FileSystem fs = getFileSystem()) {
            String sourcePath = params.get("sourcePath");
            String targetPath = params.get("targetPath");
            boolean isCopy = Boolean.parseBoolean(params.get("isCopy"));

            Path source = new Path(sourcePath);
            Path target = new Path(targetPath);

            if (!fs.exists(source)) {
                return R.error("源文件不存在");
            }

            if (fs.exists(target)) {
                return R.error("目标位置已存在同名文件");
            }

            if (isCopy) {
                FileUtil.copy(fs, source, fs, target, false, fs.getConf());
            } else {
                fs.rename(source, target);
            }

            return R.success(isCopy ? "复制成功" : "移动成功");
        } catch (IOException e) {
            return R.error("操作失败：" + e.getMessage());
        }
    }

    @GetMapping("/folders")
    public R getFolders(@RequestParam(required = false) String path) {
        try (FileSystem fs = getFileSystem()) {
            Path folderPath = path == null ? new Path("/") : new Path(path);
            if (!fs.exists(folderPath)) {
                fs.mkdirs(folderPath);
            }

            FileStatus[] fileStatuses = fs.listStatus(folderPath);
            List<Map<String, Object>> folders = new ArrayList<>();
            for (FileStatus status : fileStatuses) {
                if (status.isDirectory()) {
                    Map<String, Object> folderInfo = new HashMap<>();
                    folderInfo.put("id", status.getPath().toString());
                    folderInfo.put("label", status.getPath().getName());
                    folderInfo.put("hasChildren", fs.listStatus(status.getPath()).length > 0);
                    folders.add(folderInfo);
                }
            }
            return R.success(folders);
        } catch (IOException e) {
            return R.error("获取文件夹列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/search")
    public R searchFiles(@RequestParam String keyword) {
        try (FileSystem fs = getFileSystem()) {
            List<Map<String, Object>> results = new ArrayList<>();
            RemoteIterator<LocatedFileStatus> fileStatusIterator = fs.listFiles(new Path("/"), true);
            while (fileStatusIterator.hasNext()) {
                LocatedFileStatus status = fileStatusIterator.next();
                if (status.getPath().getName().toLowerCase().contains(keyword.toLowerCase())) {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("name", status.getPath().getName());
                    fileInfo.put("isDirectory", status.isDirectory());
                    fileInfo.put("size", status.getLen());
                    fileInfo.put("lastModified", status.getModificationTime());
                    fileInfo.put("path", status.getPath().toString());
                    results.add(fileInfo);
                }
            }
            return R.success(results);
        } catch (IOException e) {
            return R.error("搜索文件失败：" + e.getMessage());
        }
    }
}

