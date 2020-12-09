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
 * web 项目配置类
 * - 注册权限拦截器
 * - 添加跨域配置
 * - 添加静态资源映射配置
 * - 注入 RedisTemplate 实例
 *
 * @author yec
 * @date 12/6/20 6:59 PM
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    private AuthInterceptor authInterceptor;
    private AppProperties appProperties;

    @Autowired
    public void setMihoyoProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Autowired
    public void setAuthInterceptor(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * 注入 RedisTemplate 实例（其实使用 Letty 也挺好了，
     * 为了配合 spring boot 生态就... 还是搞一个 spring data redis 吧，毕竟对api的学习成本不高...
     *
     * @param redisConnectionFactory redis 连接工厂（默认letty）
     * @return RedisTemplate 实例
     */
    @Bean
    public RedisTemplate<String, LotteryUser> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, LotteryUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory); // 设置连接工厂
        redisTemplate.setKeySerializer(RedisSerializer.string());  // 设置键的序列化方式（string）
        redisTemplate.setValueSerializer(RedisSerializer.java()); // 设置值的序列化方式（字节码/二进制）
        return redisTemplate;
    }


    /**
     * 添加资源映射，将所有的 img/ 开头的请求映射到对应的文件
     *
     * @param registry 注册对象
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String getImgSaveDir = appProperties.getImgSaveDir();
        registry.addResourceHandler("img/**")
                .addResourceLocations("file:" + getImgSaveDir);
    }

    /**
     * 配置跨域请求，这里的配置和使用 @CrossOrigin 配置的都是在拦截器之后执行的。
     * 需要这里的配置生效应该在拦截器里面放行所有的 OPTIONS 请求
     *
     * @param registry 注册对象
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 映射路径
                .allowedOrigins("*") // 允许前端任意域名访问
                .allowedHeaders("vid") // 允许跨域携带自定义请求头
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH") // 允许这些请求方式
                .maxAge(3600); // 多长时间后需要再次发送 OPTION 请求检测
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
