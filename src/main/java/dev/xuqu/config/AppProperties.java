package dev.xuqu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目配置类（application.yml)
 * - 社区 cookie 信息
 * - 静态图片保存地址
 *
 * @author yec
 * @date 12/4/20 5:54 PM
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mihoyo")
public class AppProperties {
    private String accountId;
    private String cookieToken;
    private String imgSaveDir;
}
