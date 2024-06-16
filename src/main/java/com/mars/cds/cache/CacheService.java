package com.mars.cds.cache;

import java.util.Set;

/**
 * 缓存接口
 */
public interface CacheService {
    /**
     * 存入键值对数据到缓存中
     *
     * @param key   键
     * @param value 值
     * @return true表示存入成功, 否则反之
     */
    <T> boolean put(String key, T value);

    /**
     * 存入键值对数据到缓存中
     *
     * @param key   键
     * @param value 值
     * @param exp   过期时期(秒)
     * @return true表示存入成功, 否则反之
     */
    <T> boolean put(String key, T value, int exp);

    /**
     * 根据指定的键,取得对应的值
     *
     * @param key 键
     * @return 键对应的值
     */
    <T> T get(String key);

    /**
     * 模糊查询key
     *
     * @return
     */
    <T> Set<T> getKeyVague(String key);

    /**
     * 根据指定的键,删除对应的值
     *
     * @param key 键
     * @return true:表示删除成功,false:删除未成功
     */
    boolean remove(String key);

    /**
     * 设置key过期时间
     *
     * @param key 键
     * @param exp 过期时间
     * @return
     */
    boolean expire(String key, int exp);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true:表示存在KEY,false:不存在
     */
    boolean isExist(String key);
}
