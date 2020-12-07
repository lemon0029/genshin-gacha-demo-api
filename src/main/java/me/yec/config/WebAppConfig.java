package me.yec.config;

import me.yec.core.LotteryUser;
import me.yec.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yec
 * @date 12/6/20 6:59 PM
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    private AuthInterceptor authInterceptor;
    private MihoyoProperties mihoyoProperties;

    @Autowired
    public void setMihoyoProperties(MihoyoProperties mihoyoProperties) {
        this.mihoyoProperties = mihoyoProperties;
    }

    @Autowired
    public void setAuthInterceptor(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String getImgSaveDir = mihoyoProperties.getImgSaveDir();
        registry.addResourceHandler("img/**")
                .addResourceLocations("file:" + getImgSaveDir);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedHeaders("Access-Control-Allow-Origin") // 允许跨域请求
                        .allowedHeaders("Access-Control-Allow-Headers") // 允许跨域携带自定义请求头
//                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public RedisTemplate<String, LotteryUser> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, LotteryUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return redisTemplate;
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth");
    }
}
