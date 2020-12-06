package me.yec.service.impl;

import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.service.LotteryUserService;
import me.yec.service.SimpleAuthService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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

        opsForValue.set(httpSession.getId(), lotteryUser);
        return httpSession.getId();
    }

    @Override
    public LotteryUser getCurrentUser(String sessionId) {
        ValueOperations<String, LotteryUser> opsForValue = lotteryUserRedisTemplate.opsForValue();
        return opsForValue.get(sessionId);
    }
}
