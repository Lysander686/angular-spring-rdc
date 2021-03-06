package com.example.counter.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RedisStringCache {

    private static RedisStringCache redisStringCache;

    @Autowired
    @Setter
    @Getter
    private static StringRedisTemplate template;

    @Value("${cacheexpire.captcha}")
    @Setter
    @Getter
    private int captchaExpireTime;

    @Value("${cacheexpire.account}")
    @Setter
    @Getter
    private int accountExpireTime;

    @Value("${cacheexpire.order}")
    @Setter
    @Getter
    private int orderExpireTime;


    @PostConstruct
    private void init() {
        redisStringCache = new RedisStringCache();
        redisStringCache.setTemplate(template);
        redisStringCache.setCaptchaExpireTime(captchaExpireTime);
        redisStringCache.setAccountExpireTime(accountExpireTime);
        redisStringCache.setOrderExpireTime(orderExpireTime);
    }

    //增加缓存
    public static void cache(String key, String value, com.example.counter.cache.CacheType cacheType) {
        int expireTime;
        switch (cacheType) {
            case ACCOUNT:
                expireTime = redisStringCache.getAccountExpireTime();
                break;
            case CAPTCHA:
                expireTime = redisStringCache.getCaptchaExpireTime();
                break;
            case ORDER:
            case TRADE:
            case POSI:
                expireTime = redisStringCache.getOrderExpireTime();
                break;
            default:
                expireTime = 10;
        }

        redisStringCache.getTemplate()
                .opsForValue().set(cacheType.type() + key, value
                , expireTime, TimeUnit.SECONDS);
    }

    //查询缓存
    public static String get(String key, com.example.counter.cache.CacheType cacheType) {
        return redisStringCache.getTemplate()
                .opsForValue().get(cacheType.type() + key);
    }

    //删除缓存
    public static void remove(String key, CacheType cacheType) {
        redisStringCache.getTemplate()
                .delete(cacheType.type() + key);
    }

}
