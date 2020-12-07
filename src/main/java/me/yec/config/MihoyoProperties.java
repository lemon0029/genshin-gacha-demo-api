package me.yec.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yec
 * @date 12/4/20 5:54 PM
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mihoyo")
public class MihoyoProperties {
    private String accountId;
    private String cookieToken;
    private String imgSaveDir;
}
