package me.yec.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.yec.core.LotteryUser;
import me.yec.model.support.Result;
import org.apache.http.HttpStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        // 允许跨域请求（前端在请求头里面携带了 vid 这个字段）
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 从请求头中取出 vid 即可
        String vid = request.getHeader("vid");
        ValueOperations<String, LotteryUser> ops = lotteryUserRedisTemplate.opsForValue();
        if (vid == null || ops.get(vid) == null) {
            Result<Object> error = Result.error(HttpStatus.SC_UNAUTHORIZED, "unauthorized", null);
            ServletOutputStream outputStream = response.getOutputStream();
            mapper.writeValue(outputStream, error);
            return false;
        } else {
            return true;
        }
    }
}
