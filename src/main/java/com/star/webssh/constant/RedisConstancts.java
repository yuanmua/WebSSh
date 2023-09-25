package com.star.webssh.constant;

/**
 *redis中key常量
 * @author Mr.Wang
 * @create 2023-09-24-20:26
 */
public interface RedisConstancts {
    public static final String LOGIN_CODE_KEY="login:code:";
    Long LONG_CODE_TTL=2l;

   String LONG_CODE_USER_KEY="login:token:";
    Long LONG_CODE_USER_TTL=30L;
}
