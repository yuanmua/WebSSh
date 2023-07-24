package com.star.webssh.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类
 * @author Mr.Wang
 * @create 2023-06-05-20:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private Object data;

    private Map map=new HashMap();//动态数据

    public static R success() {
        return new R(1,"success",null,null);
    }
    public  static  R success(Object data) {
        return new R(1,"success",data,null);
    }
    public static R error(String msg){
        return new R(0,msg,null,null);
    }

    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }

}
