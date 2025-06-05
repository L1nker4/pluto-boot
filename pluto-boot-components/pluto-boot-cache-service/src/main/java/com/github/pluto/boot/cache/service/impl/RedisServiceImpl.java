package com.github.pluto.boot.cache.service.impl;

import com.github.pluto.boot.cache.entity.RedisInfo;
import com.github.pluto.boot.cache.exception.RedisConnectException;
import com.github.pluto.boot.cache.service.RedisService;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisServerCommands;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 16:27
 */
public class RedisServiceImpl implements RedisService {

    private static final String separator = System.lineSeparator();

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

    @Override
    public List<RedisInfo> getRedisInfo() throws RedisConnectException {
        String info = redisTemplate.execute((RedisCallback<String>) connection -> {
            StatefulRedisConnection<?, ?> nativeConnection =
                    (StatefulRedisConnection<?, ?>) connection.getNativeConnection();
            RedisServerCommands<?, ?> sync = nativeConnection.sync();
            return sync.info();
        });
        return getRedisInfoList(info);
    }

    private List<RedisInfo> getRedisInfoList(String info) {
        List<RedisInfo> infoList = new ArrayList<>();
        String[] strs = Objects.requireNonNull(info).split(separator);
        RedisInfo redisInfo;
        for (String str1 : strs) {
            redisInfo = new RedisInfo();
            String[] str = str1.split(":");
            if (str.length > 1) {
                String key = str[0];
                String value = str[1];
                redisInfo.setKey(key);
                redisInfo.setValue(value);
                infoList.add(redisInfo);
            }
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() throws RedisConnectException {
        Long dbSize = redisTemplate.execute((RedisCallback<Long>) connection -> {
            StatefulRedisConnection<?, ?> nativeConnection =
                    (StatefulRedisConnection<?, ?>) connection.getNativeConnection();
            RedisServerCommands<?, ?> sync = nativeConnection.sync();
            return sync.dbsize();
        });

        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        String info = redisTemplate.execute((RedisCallback<String>) connection -> {
            StatefulRedisConnection<?, ?> nativeConnection =
                    (StatefulRedisConnection<?, ?>) connection.getNativeConnection();
            RedisServerCommands sync = nativeConnection.sync();
            return sync.info("memory"); // 只请求 memory 部分，更高效
        });

        if (info == null) {
            throw new RedisConnectException("无法获取 Redis info 信息");
        }

        String[] lines = info.split(separator);
        Map<String, Object> map = null;
        for (String line : lines) {
            if (line.startsWith("used_memory:")) {
                String[] detail = line.split(":");
                if (detail.length == 2) {
                    map = new HashMap<>();
                    map.put("used_memory", detail[1].trim()); // 不再截断最后一位，直接 trim
                    map.put("create_time", System.currentTimeMillis());
                }
                break;
            }
        }
        return map;
    }

}
