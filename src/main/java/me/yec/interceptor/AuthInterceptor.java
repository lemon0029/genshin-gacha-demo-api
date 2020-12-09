package me.yec.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yec.core.LotteryUser;
import me.yec.model.support.Result;
import org.apache.http.HttpStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限认证拦截器
 * 除了 /api/auth 接口外，其它任何接口都需要携带 vid 请求头来获取资源
 *
 * @author yec
 * @date 12/6/20 8:02 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final RedisTemplate<String, LotteryUser> lotteryUserRedisTemplate;
    private final ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 放行所有的 OPTIONS 请求，让 CORS 配置生效
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        // 从请求头中取出 vid 即可
        String vid = request.getHeader("vid");
        ValueOperations<String, LotteryUser> ops = lotteryUserRedisTemplate.opsForValue();
        if (vid == null || ops.get(vid) == null) { // 短路与运算
            Result<Object> error = Result.error(HttpStatus.SC_UNAUTHORIZED, "unauthorized", null);
            mapper.writeValue(response.getOutputStream(), error);
            return false;
        } else {
            return true;
        }
    }
}
