package com.kuafu.dify.config;

import com.kuafu.web.dify.ConfigService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "dify")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifyConfig {
    @Resource
    private ConfigService configService;

    private String apiKey = "app-hkccEmKICf7D8yv1gcDDgclP"; // 从配置文件中获取

    private String baseUrl = "http://60.204.199.245:6680/v1";

    private String apiUrl = "http://60.204.199.245:6680/v1/chat-messages";  // 从配置文件中获取

    private String uploadUrl = "http://60.204.199.245:6680/v1/file/upload";  // 从配置文件中获取

    private String prompt = "你是一名小助手";

    @PostConstruct
    public void initFromDatabase() {
        Map<String, String> configMap = configService.loadDifyConfig();

        this.apiKey = configMap.getOrDefault("apiKey", this.apiKey);
        this.baseUrl = configMap.getOrDefault("baseUrl", this.baseUrl);
        this.apiUrl = configMap.getOrDefault("apiUrl", this.apiUrl);
        this.uploadUrl = configMap.getOrDefault("uploadUrl", this.uploadUrl);
        this.prompt = configMap.getOrDefault("prompt", this.prompt);

        System.out.println("DifyConfig initialized: " + this);
    }
}
