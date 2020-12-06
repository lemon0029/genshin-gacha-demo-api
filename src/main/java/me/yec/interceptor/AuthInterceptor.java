package me.yec.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.model.support.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yec
 * @date 12/6/20 8:02 PM
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final RedisTemplate<String, LotteryUser> lotteryUserRedisTemplate;
    private final ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        ValueOperations<String, LotteryUser> ops = lotteryUserRedisTemplate.opsForValue();
        LotteryUser lotteryUser = ops.get(session.getId());
        if (lotteryUser == null) {
            Result<Object> error = Result.error(null);
            ServletOutputStream outputStream = response.getOutputStream();
            mapper.writeValue(outputStream, error);
            return false;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
}
