package com.star.webssh.common;

/**
 * 基于ThreadLocal封装工具类，用户和获取当前登录用户id
 * @author Mr.Wang
 * @create 2023-06-12-16:59
 */
public class BaseContext {

    //private static Long id;
    private static ThreadLocal<Long> threadLocal= new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
        //BaseContext.id=id;
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){

        //return id;
        return threadLocal.get();

    }

    public static void removeId(){

        //移除当前线程中的值
         threadLocal.remove();

    }


}
