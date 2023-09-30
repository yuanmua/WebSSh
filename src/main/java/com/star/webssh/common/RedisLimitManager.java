package com.star.webssh.common;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 专门提供RedisLimiter 限流基础服务的（提供了通用的功能）
 * @author Mr.Wang
 * @create 2023-09-10-12:18
 */
@Service//表示该类是一个bean
public class RedisLimitManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     * @param key 区分不同的限流器，比如不同用户的id应该分别统计
     */
    public void doRateLimit(String key) {

        // 创建RateLimiter实例
        RRateLimiter rateLimit = redissonClient.getRateLimiter(key);

        // 创建用户访问计数器，RateType.OVERALL优先选择OVERALL类型，表示不管在几台机子上跑，都是以总的条数来计算
        //rate:5 ，rateInterval 1 表示每秒生成了5个令牌
        rateLimit.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        //当每一个操作来了之后，请求一个令牌
        boolean canOp = rateLimit.tryAcquire(2);//每个操作占用两个令牌
        if (!canOp){
            throw new CustomException("请求过于频繁");
        }
    }
}