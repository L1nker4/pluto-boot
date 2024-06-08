package com.github.pluto.boot.cache.service;


import java.util.Set;


/**
 * @author     ：L1nker4
 * @date       ： 创建于  2024/05/26 16:39
 * @description： Redis服务
 */
public interface RedisService {


    /**
     * 获取 key
     *
     * @param pattern 正则
     * @return Set
     */
    Set<String> getKeys(String pattern);

    /**
     * get命令
     *
     * @param key key
     * @return String
     */
    String get(String key);

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     */
    void set(String key, String value);

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param milliSeconds 毫秒
     */
    void set(String key, String value, Long milliSeconds);

    void del(String key);

    /**
     * del命令
     *
     * @param keys keys
     */
    void del(String... keys);

    /**
     * exists命令
     *
     * @param key key
     * @return Boolean
     */
    Boolean exists(String key);

    /**
     * zadd 命令
     *
     * @param key    key
     * @param score  score
     * @param value value
     */
    Boolean zadd(String key, Double score, String value);

    /**
     * zrangeByScore 命令
     *
     * @param key key
     * @param min min
     * @param max max
     * @return Set<String>
     */
    Set<String> zrangeByScore(String key, Double min, Double max);

    /**
     * zremrangeByScore 命令
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return Long
     */
    Long zremrangeByScore(String key, Double start, Double end);

    /**
     * zrem 命令
     *
     * @param key     key
     * @param members members
     * @return Long
     */
    Long zrem(String key, String... members);

    /**
     * 获取一个分布式锁
     * @param lockName 锁名称
     * @param timeout 超时时间，单位：s
     * @return
     */
    Boolean acquireDistributedLock(String lockName, Integer timeout);

    /**
     * 释放一个分布式锁
     * @param lockName 锁名称
     */
    void releaseDistributedLock(String lockName);
}
