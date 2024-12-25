package com.star.webssh.controller;

import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.*;

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

    // 在类的开头添加文件名验证的正则表达式
    private static final String VALID_FILENAME_REGEX = "^[^\\\\/:*?\"<>|]+$";

    private final HttpServletRequest httpServletRequest;

    public FileController(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    private boolean isValidFileName(String fileName) {
        return fileName != null && fileName.matches(VALID_FILENAME_REGEX);
    }

    private boolean isFileExists(String dirPath, String fileName) {
        Path path = Paths.get(dirPath, fileName);
        return Files.exists(path);
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

    private long getFolderSize(Path folder) {
        try {
            return Files.walk(folder)
                    .filter(p -> !Files.isDirectory(p))
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            return 0L;
        }
    }

    @GetMapping("/list")
    public R list(@RequestParam(required = false) String path) {
        try {
            Long userId = getUserId();
            String userRootPath = uploadPath + "/users/" + userId;
            String currentPath = (path == null || path.isEmpty()) ? userRootPath : userRootPath + "/" + path;

            Files.createDirectories(Paths.get(currentPath));

            List<Map<String, Object>> files = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(currentPath))) {
                for (Path entry : stream) {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("name", entry.getFileName().toString());
                    boolean isDir = Files.isDirectory(entry);
                    fileInfo.put("isDirectory", isDir);
                    fileInfo.put("size", isDir ? getFolderSize(entry) : Files.size(entry));
                    fileInfo.put("lastModified", Files.getLastModifiedTime(entry).toMillis());
                    files.add(fileInfo);
                }
            }
            return R.success(files);
        } catch (RuntimeException e) {
            return R.error("NOT_LOGIN");
        } catch (IOException e) {
            return R.error("获取文件列表失败：" + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String path) {
        try {
            String fileName = file.getOriginalFilename();
            if (!isValidFileName(fileName)) {
                return R.error("文件名不能包含特殊字符: \\\\ / : * ? \" < > | ");
            }

            Long userId = getUserId();
            String userRootPath = uploadPath + "/users/" + userId;
            String targetPath = (path == null || path.isEmpty()) ? userRootPath : userRootPath + "/" + path;

            if (isFileExists(targetPath, fileName)) {
                return R.error("文件已存在");
            }

            Files.createDirectories(Paths.get(targetPath));
            Files.copy(file.getInputStream(), Paths.get(targetPath, fileName), StandardCopyOption.REPLACE_EXISTING);

            return R.success("文件上传成功");
        } catch (RuntimeException e) {
            return R.error("NOT_LOGIN");
        } catch (IOException e) {
            return R.error("文件上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam String path, HttpServletResponse response) throws IOException {
        Long userId = getUserId();
        String filePath = uploadPath + "/users/" + userId + "/" + path;
        Path pathToDownload = Paths.get(filePath);

        if (!Files.exists(pathToDownload)) {
            throw new FileNotFoundException("文件不存在");
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + pathToDownload.getFileName().toString());
        Files.copy(pathToDownload, response.getOutputStream());
    }


    // 创建文件夹
    @PostMapping("/createFolder")
    public R createFolder(@RequestBody Map<String, String> params) {
        try {
            String path = params.get("path");
            String folderName = params.get("folderName");
            
            // 验证文件夹名称
            if (!isValidFileName(folderName)) {
                return R.error("文件夹名称不能包含特殊字符: \\ / : * ? \" < > |");
            }
            
            Long userId = getUserId();
            String fullPath = uploadPath + "/users/" + userId;
            
            // 如果有子路径，添加到路径中
            if (path != null && !path.isEmpty()) {
                fullPath = fullPath + "/" + path;
            }
            
            // 检查文件夹是否已存在
            if (isFileExists(fullPath, folderName)) {
                return R.error("文件夹已存在");
            }
            
            // 创建文件夹
            Files.createDirectories(Paths.get(fullPath, folderName));
            return R.success("文件夹创建成功");
        } catch (IOException e) {
            return R.error("创建文件夹失败：" + e.getMessage());
        }
    }

    // 重命名文件/文件夹
    @PostMapping("/rename")
    public R rename(@RequestBody Map<String, String> params) {
        try {
            String path = params.get("path");
            String oldName = params.get("oldName");
            String newName = params.get("newName");
            
            // 验证新文件名
            if (!isValidFileName(newName)) {
                return R.error("文件名不能包含特殊字符: \\ / : * ? \" < > |");
            }
            
            Long userId = getUserId();
            String basePath = uploadPath + "/users/" + userId;
            
            // 如果有子路径，添加到路径中
            if (path != null && !path.isEmpty()) {
                basePath = basePath + "/" + path;
            }
            
            // 检查新文件名是否已存在
            if (isFileExists(basePath, newName)) {
                return R.error("文件名已存��");
            }
            
            Path source = Paths.get(basePath, oldName);
            Path target = Paths.get(basePath, newName);
            
            // 检查源文件是否存在
            if (!Files.exists(source)) {
                return R.error("源文件不存在");
            }
            
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return R.success("重命名成功");
        } catch (IOException e) {
            return R.error("重命名失败：" + e.getMessage());
        }
    }

    // 删除文件/文件夹
    @DeleteMapping("/delete")
    public R delete(@RequestParam String path) {
        try {
            Long userId = getUserId();
            String fullPath = uploadPath + "/users/" + userId + "/" + path;
            Path pathToDelete = Paths.get(fullPath);
            
            if (Files.isDirectory(pathToDelete)) {
                Files.walk(pathToDelete)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            } else {
                Files.delete(pathToDelete);
            }
            return R.success("删除成功");
        } catch (IOException e) {
            return R.error("删除失败：" + e.getMessage());
        }
    }

    // 移动或复制文件/文件夹
    @PostMapping("/move")
    public R move(@RequestBody Map<String, String> params) {
        try {
            String sourcePath = params.get("sourcePath");
            String targetPath = params.get("targetPath");
            boolean isCopy = Boolean.parseBoolean(params.get("isCopy"));

            Long userId = getUserId();
            String userRoot = uploadPath + "/users/" + userId;
            Path source = Paths.get(userRoot, sourcePath);
            Path target = Paths.get(userRoot, targetPath);

            // 检查源文件是否存在
            if (!Files.exists(source)) {
                return R.error("源文件不存在");
            }

            // 检查目标路径是否已存在同名文件
            if (Files.exists(target)) {
                return R.error("目标位置已存在同名文件");
            }

            // 确保目标父目录存在
            Files.createDirectories(target.getParent());

            if (isCopy) {
                if (Files.isDirectory(source)) {
                    // 复制目录
                    Files.walk(source).forEach(s -> {
                        try {
                            Path d = target.resolve(source.relativize(s));
                            if (Files.isDirectory(s)) {
                                Files.createDirectories(d);
                            } else {
                                Files.copy(s, d, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    // 复制文件
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                // 移动文件或目录
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            }

            return R.success(isCopy ? "复制成功" : "移动成功");
        } catch (IOException e) {
            return R.error((params.get("isCopy").equals("true") ? "复制" : "移动") + "失败：" + e.getMessage());
        }
    }

    // 添加获取文件夹树的方法
    @GetMapping("/folders")
    public R getFolders(@RequestParam(required = false) String path) {
        try {
            Long userId = getUserId();
            String userRootPath = uploadPath + "/users/" + userId;
            String currentPath = userRootPath;
            
            if (path != null && !path.isEmpty()) {
                currentPath = userRootPath + "/" + path;
            }

            // 确保目录存在
            Files.createDirectories(Paths.get(currentPath));

            // 只获取文件夹
            List<Map<String, Object>> folders = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(currentPath))) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        Map<String, Object> folderInfo = new HashMap<>();
                        String relativePath = path == null ? entry.getFileName().toString() 
                            : path + "/" + entry.getFileName().toString();
                        
                        folderInfo.put("id", relativePath);
                        folderInfo.put("label", entry.getFileName().toString());
                        // 检查是否有子文件夹
                        folderInfo.put("hasChildren", hasSubfolders(entry));
                        folders.add(folderInfo);
                    }
                }
            }
            return R.success(folders);
        } catch (IOException e) {
            return R.error("获取文件夹列表失败：" + e.getMessage());
        }
    }

    // 检查是否有子文件夹
    private boolean hasSubfolders(Path directory) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 修改搜索方法
    @GetMapping("/search")
    public R searchFiles(@RequestParam String keyword) {
        try {
            Long userId = getUserId();
            String userRootPath = uploadPath + "/users/" + userId;
            Path rootPath = Paths.get(userRootPath);
            
            if (!Files.exists(rootPath)) {
                return R.success(new ArrayList<>());
            }

            List<Map<String, Object>> results = new ArrayList<>();
            Files.walk(rootPath)
                .filter(path -> {
                    String fileName = path.getFileName().toString().toLowerCase();
                    return fileName.contains(keyword.toLowerCase());
                })
                .forEach(path -> {
                    try {
                        Map<String, Object> fileInfo = new HashMap<>();
                        boolean isDir = Files.isDirectory(path);
                        
                        // 获取相对于用户根目录的路径
                        String relativePath = rootPath.relativize(path).toString().replace("\\", "/");
                        
                        // 获取父目录路径（相对于用户根目录）
                        String parentPath = "";
                        if (path.getParent() != null && !path.getParent().equals(rootPath)) {
                            parentPath = rootPath.relativize(path.getParent()).toString().replace("\\", "/");
                        }
                        
                        fileInfo.put("name", path.getFileName().toString());
                        fileInfo.put("isDirectory", isDir);
                        fileInfo.put("size", isDir ? getFolderSize(path) : Files.size(path));
                        fileInfo.put("lastModified", Files.getLastModifiedTime(path).toMillis());
                        fileInfo.put("path", relativePath);
                        fileInfo.put("parentPath", parentPath);
                        results.add(fileInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            return R.success(results);
        } catch (IOException e) {
            return R.error("搜索文件失败：" + e.getMessage());
        }
    }
}


