package com.mars.cds.cache;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundListOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class CacheServiceTest {

    @Resource
    private RedisClientBean redisClientBean;

    @Test
    public void testCache() {
        String key = "aaa";
        redisClientBean.put(key, "uu");
        String value = redisClientBean.get(key);
        System.out.println(value);
    }

    @Test
    public void testAddLogin() {
        BoundListOperations<String, String> userBoundList = redisClientBean.getRedisTemplate().boundListOps("app.list.111");
        userBoundList.rightPush("1");
        userBoundList.rightPush("2");
        userBoundList.rightPush("3");
        userBoundList.rightPush("2");
    }

    @Test
    public void testRemoveLogin() {
        BoundListOperations<String, String> userBoundList = redisClientBean.getRedisTemplate().boundListOps("app.list.111");
        System.out.println("size===========" + userBoundList.size());
        //userBoundList.remove(1, "1");
        userBoundList.expire(0, TimeUnit.SECONDS);
    }
}
