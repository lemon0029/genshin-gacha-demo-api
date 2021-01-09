package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.exception.AppException;
import me.yec.service.LotteryUserService;
import me.yec.service.SimpleAuthService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @author yec
 * @date 12/6/20 8:06 PM
 */
@Service
@RequiredArgsConstructor
public class SimpleAuthServiceImpl implements SimpleAuthService {

    private final LotteryUserService lotteryUserService;
    private final RedisTemplate<String, LotteryUser> lotteryUserRedisTemplate;
    private final HttpSession httpSession;


    @Override
    public String doAuth() {
        ValueOperations<String, LotteryUser> opsForValue = lotteryUserRedisTemplate.opsForValue();
        LotteryUser lotteryUser = opsForValue.get(httpSession.getId());

        if (lotteryUser == null) {
            lotteryUser = lotteryUserService.init();
        }

        // 默认保存一天
        opsForValue.set(httpSession.getId(), lotteryUser, 1L, TimeUnit.DAYS);
        return httpSession.getId();
    }

    @Override
    public LotteryUser getCurrentUser(String vid) {
        ValueOperations<String, LotteryUser> opsForValue = lotteryUserRedisTemplate.opsForValue();
        LotteryUser lotteryUser = opsForValue.get(vid);

        // 虽然拦截器会每次查找数据库中有没有这个 vid 对应的用户
        // 为了保险期间，这里还是做个非空判断
        if (lotteryUser == null) {
            throw new AppException(vid + " is not fount");
        }
        return lotteryUser;
    }

    @Override
    public void updateUser(String vid, LotteryUser lotteryUser) {
        ValueOperations<String, LotteryUser> opsForValue = lotteryUserRedisTemplate.opsForValue();
        Long expire = lotteryUserRedisTemplate.getExpire(vid);

        if (expire != null) {
            if (expire < 0) {
                // 一般只会是 -1 吧（没有过期时间）
                opsForValue.set(vid, lotteryUser);
            } else {
                // 存在过期时间则不变（不太严谨....）
                opsForValue.set(vid, lotteryUser, expire, TimeUnit.SECONDS);
            }
        }
    }
}
