package com.mars.cds.cache;

import com.mars.cds.support.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis实现缓存
 */
@Service
public class RedisClientBean implements CacheService {
    final static Logger log = LoggerFactory.getLogger(RedisClientBean.class.getSimpleName());

    @Resource
    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public <T> boolean put(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            LogUtils.error(log, "invoke put error", e, key);
            return false;
        }
    }

    @Override
    public <T> boolean put(String key, T value, int exp) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, exp, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            LogUtils.error(log, "invoke put.exp error", e, key, exp);
            return false;
        }
    }

    /**
     * 存入键值对数据到缓存中 并设置指定的预期时间
     *
     * @param key               键
     * @param value             值
     * @param expectedTimeStamp 指定的预期时间戳
     * @param <T>
     * @return 是否成功
     */
    public <T> boolean putExpireAt(String key, T value, Date expectedTimeStamp) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return redisTemplate.expireAt(key, expectedTimeStamp);
        } catch (Exception e) {
            LogUtils.error(log, "invoke put.exp error", e, key, expectedTimeStamp.getTime());
            return false;
        }
    }

    /**
     * 获取 key 的预期 时间，精确到毫秒
     *
     * @param key 键
     * @param <T>
     * @return 毫秒
     */
    public <T> long getExpireAt(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }


    @Override
    public <T> T get(String key) {
        try {
            return key == null ? null : (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            LogUtils.error(log, "invoke get error", e, key);
            return null;
        }

    }

    @Override
    public <T> Set<T> getKeyVague(String key) {
        return redisTemplate.keys(key);
    }

    @Override
    public boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            LogUtils.error(log, "invoke remove error", e, key);
            return false;
        }
    }

    public boolean removeVague(String key) {
        try {
            Set<String> keys = redisTemplate.keys(key);
            redisTemplate.delete(keys);
            return true;
        } catch (Exception e) {
            LogUtils.error(log, "invoke removeVague error", e, key);
            return false;
        }
    }

    @Override
    public boolean expire(String key, int exp) {
        try {
            if (exp > 0)
                redisTemplate.expire(key, exp, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            LogUtils.error(log, "invoke expire error", e, key, exp);
            return false;
        }
    }

    @Override
    public boolean isExist(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            LogUtils.error(log, "invoke isExist error", e, key);
            return false;
        }
    }
}
