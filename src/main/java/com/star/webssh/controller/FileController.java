package com.star.webssh.controller;

import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import io.jsonwebtoken.Claims;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;
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

    // 测试Hadoop连接的API端点
    @GetMapping("/test-hadoop-connection")
    public R testHadoopConnection() {
        try {
            FileSystem fs = getFileSystem();
            // 尝试列出根目录的内容，如果这成功，那么我们可以认为连接是成功的
            FileStatus[] rootStatuses = fs.listStatus(new Path("/"));
            if (rootStatuses != null) {
                return R.success("成功连接到Hadoop");
            } else {
                return R.error("Hadoop连接测试失败：无法获取根目录内容");
            }
        } catch (IOException e) {
            return R.error("Hadoop连接失败：" + e.getMessage());
        }
    }

    private static final String VALID_FILENAME_REGEX = "^[^\\\\/:*?\"<>|]+$";

    // 添加文件名验证方法
    private boolean isValidFileName(String fileName) {
        return fileName != null && fileName.matches(VALID_FILENAME_REGEX);
    }


    private FileSystem getFileSystem() throws IOException {
        Configuration conf = new Configuration();
        // 加载core-site.xml和hdfs-site.xml配置文件
        conf.addResource(new Path("classpath:/core-site.xml"));
        conf.addResource(new Path("classpath:/hdfs-site.xml"));
        // 设置文件系统URI
        conf.set("fs.defaultFS", hdfsUri);
        conf.set("dfs.client.use.datanode.hostname", "true");
        conf.set("dfs.datanode.username", "root");
        conf.set("dfs.datanode.password", "123456");
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

            // 确保用户目录存在
            Path dirPath = new Path(path == null ? userPath : userPath + "/" + path);
            if (!fs.exists(new Path(userPath))) {
                fs.mkdirs(new Path(userPath));  // 如果用户目录不存在，则创建
                fs.setPermission(new Path(userPath), FsPermission.valueOf("drwxrwxrwx"));  // 设置权限为777
            }

            if (!fs.exists(dirPath)) {
                return R.error("目录不存在");
            }

            // 获取目录中文件信息
            FileStatus[] statuses = fs.listStatus(dirPath);
            List<Map<String, Object>> files = new ArrayList<>();

            for (FileStatus status : statuses) {
                // 对每个文件/文件夹设置权限
                fs.setPermission(status.getPath(), FsPermission.valueOf("drwxrwxrwx"));

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


    // 文件上传方法
    @PostMapping("/upload")
    public R uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String path) {
        try (FileSystem fs = getFileSystem()) {
            Long userId = getUserId();
            String userPath = "/users/" + userId;

            // 确保用户目录存在
            Path dirPath = new Path(path == null ? userPath : userPath + "/" + path);
            if (!fs.exists(new Path(userPath))) {
                fs.mkdirs(new Path(userPath));  // 如果用户目录不存在，则创建
                fs.setPermission(new Path(userPath), FsPermission.valueOf("drwxrwxrwx"));
            }

            if (!fs.exists(dirPath)) {
                fs.mkdirs(dirPath);  // 确保目标目录存在
                fs.setPermission(dirPath, FsPermission.valueOf("drwxrwxrwx"));
            }

            Path filePath = new Path(dirPath, file.getOriginalFilename());

            if (fs.exists(filePath)) {
                return R.error("文件已存在");
            }

            // 上传文件
            try (FSDataOutputStream out = fs.create(filePath)) {
                out.write(file.getBytes());
            }

            // 上传后设置文件权限
            fs.setPermission(filePath, FsPermission.valueOf("drwxrwxrwx"));

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
            String sourcePath = "/users/" + getUserId() + "/" + params.get("sourcePath");
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

    @PostMapping("/rename")
    public R rename(@RequestBody Map<String, String> params) {
        try (FileSystem fs = getFileSystem()) {
            String path = params.get("path");
            String oldName = params.get("oldName");
            String newName = params.get("newName");

            // 验证新文件名是否合法
            if (!isValidFileName(newName)) {
                return R.error("文件名不能包含特殊字符: \\ / : * ? \" < > |");
            }

            Long userId = getUserId();
            String basePath = "/users/" + userId;

            // 如果有子路径，添加到路径中
            if (path != null && !path.isEmpty()) {
                basePath = basePath + "/" + path;
            }

            // 源文件和目标文件路径
            Path sourcePath = new Path(basePath, oldName);
            Path targetPath = new Path(basePath, newName);

            // 检查源文件是否存在
            if (!fs.exists(sourcePath)) {
                return R.error("源文件不存在");
            }

            // 检查目标文件是否已存在
            if (fs.exists(targetPath)) {
                return R.error("目标文件已存在");
            }

            // 执行重命名操作
            boolean success = fs.rename(sourcePath, targetPath);
            if (success) {
                return R.success("重命名成功");
            } else {
                return R.error("重命名失败");
            }
        } catch (IOException e) {
            return R.error("重命名失败：" + e.getMessage());
        }
    }


    // 创建文件夹
    @PostMapping("/createFolder")
    public R createFolder(@RequestBody Map<String, String> params) {
        try (FileSystem fs = getFileSystem()) {
            String path = params.get("path");
            String folderName = params.get("folderName");

            // 验证文件夹名称
            if (!isValidFileName(folderName)) {
                return R.error("文件夹名称不能包含特殊字符: \\ / : * ? \" < > |");
            }

            Long userId = getUserId();
            String fullPath = "/users/" + userId;

            // 如果有子路径，添加到路径中
            if (path != null && !path.isEmpty()) {
                fullPath = fullPath + "/" + path;
            }

            // 检查文件夹是否已存在
            Path folderPath = new Path(fullPath, folderName);
            if (fs.exists(folderPath)) {
                return R.error("文件夹已存在");
            }

            // 创建文件夹
            if (fs.mkdirs(folderPath)) {
                // 设置权限为 drwxrwxrwx
                fs.setPermission(folderPath, FsPermission.valueOf("drwxrwxrwx"));
                return R.success("文件夹创建成功");
            } else {
                return R.error("创建文件夹失败");
            }
        } catch (IOException e) {
            return R.error("创建文件夹失败：" + e.getMessage());
        }
    }



    @GetMapping("/folders")
    public R getFolders(@RequestParam(required = false) String path) {
        try (FileSystem fs = getFileSystem()) {
            // 获取当前用户目录路径
            Long userId = getUserId();
            String userPath = "/users/" + userId;
            Path folderPath = (path == null || path.trim().isEmpty()) ? new Path(userPath) : new Path(userPath + "/" + path);

            if (!fs.exists(folderPath)) {
                fs.mkdirs(folderPath);
                fs.setPermission(folderPath, FsPermission.valueOf("drwxrwxrwx"));
            }

            // 获取指定路径下的所有文件和文件夹
            List<Map<String, Object>> folderTree = getFolderTree(fs, folderPath);
            return R.success(folderTree);
        } catch (IOException e) {
            return R.error("获取文件夹列表失败：" + e.getMessage());
        }
    }

    // 递归获取文件夹树的方法
    private List<Map<String, Object>> getFolderTree(FileSystem fs, Path folderPath) throws IOException {
        List<Map<String, Object>> folderInfoList = new ArrayList<>();

        // 获取当前目录下的所有文件和文件夹
        FileStatus[] fileStatuses = fs.listStatus(folderPath);
        for (FileStatus status : fileStatuses) {
            if (status.isDirectory()) {
                Map<String, Object> folderInfo = new HashMap<>();
                folderInfo.put("id", status.getPath().toString());
                folderInfo.put("label", status.getPath().getName());
                folderInfo.put("hasChildren", fs.listStatus(status.getPath()).length > 0);

                // 如果该文件夹有子文件夹，递归获取其子文件夹树
                List<Map<String, Object>> subFolders = getFolderTree(fs, status.getPath());
                folderInfo.put("children", subFolders);

                folderInfoList.add(folderInfo);
            }
        }
        return folderInfoList;
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

