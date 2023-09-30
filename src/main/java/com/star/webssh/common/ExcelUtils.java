package com.star.webssh.common;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel相关工具类
 * @author Mr.Wang
 * @create 2023-09-02-10:37
 */
@Slf4j
public class ExcelUtils {
    /**
     * excel转csv
     * @param multipartFile
     * @return
     */
    public static String excelToCsv(MultipartFile multipartFile)  {
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:test_excel.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        //读取数据
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误",e);
        }
        if(CollUtil.isEmpty(list)){
            return "";
        }

        //转化为csv
        StringBuilder stringBuilder = new StringBuilder();//线程不安全但快
        //读取表头,将其转化为LinkedHashMap因为其表示有序的线性的集合，而Map是乱序存储的
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        //过滤掉为null的数据
        //List<String> herderList = headerMap.values().stream().filter(header -> ObjectUtils.isNotEmpty(header)).collect(Collectors.toList());

        List<String> herderList = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        //字符串拼接
       stringBuilder.append(StringUtils.join(herderList,",")).append("\n");



        //读数据
        for (int i = 1; i <list.size() ; i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            //过滤为null的数据
            List<String> dataList = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList,",")).append("\n");
        }
        return stringBuilder.toString();
    }


}
