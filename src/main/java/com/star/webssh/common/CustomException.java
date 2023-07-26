package com.star.webssh.common;

/**
 * 自定义异常用于输出异常信息
 * @author Mr.Wang
 * @create 2023-07-26-23:46
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){ super(message);}
}
