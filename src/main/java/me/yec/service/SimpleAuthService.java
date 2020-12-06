package me.yec.service;

import me.yec.core.LotteryUser;

/**
 * @author yec
 * @date 12/6/20 8:06 PM
 */
public interface SimpleAuthService {
    String doAuth();

    LotteryUser getCurrentUser(String sessionId);
}
