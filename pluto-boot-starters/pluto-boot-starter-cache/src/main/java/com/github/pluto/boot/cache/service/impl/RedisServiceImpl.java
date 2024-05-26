package com.github.pluto.boot.cache.service.impl;

import com.github.pluto.boot.cache.service.RedisService;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 16:27
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, Long milliSeconds) {
        redisTemplate.opsForValue().set(key, value, milliSeconds, TimeUnit.MICROSECONDS);
    }

    @Override
    public void del(String... keys) {
        for (String key : keys) {
            redisTemplate.opsForValue().getAndDelete(key);
        }
    }

    @Override
    public void del(String key) {
        redisTemplate.opsForValue().getAndDelete(key);
    }

    @Override
    public Boolean exists(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        return object != null;
    }

    @Override
    public Boolean zadd(String key, Double score, String value) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Set<String> zrangeByScore(String key, Double min, Double max) {
        Set<Object> set = redisTemplate
                .opsForZSet()
                .rangeByScore(key, min, max);
        if (CollectionUtils.isEmpty(set)){
            return Set.of();
        }
        return set
                .stream()
                .map(item -> (String) item)
                .collect(Collectors.toSet());
    }

    @Override
    public Long zremrangeByScore(String key, Double start, Double end) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, start, end);
    }

    @Override
    public Long zrem(String key, String... members) {
        return redisTemplate.opsForZSet().remove(key, members);
    }

    @Override
    public Boolean acquireDistributedLock(String lockName, Integer timeout) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            return lock.tryLock(timeout, 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void releaseDistributedLock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
