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

    private String apiKey;

    private String baseUrl;

    private String apiUrl;

    private String uploadUrl;

    private String prompt;

    private String isTenant;

    public void initFromDatabase() {
        Map<String, String> configMap = configService.loadDifyConfig();

        this.apiKey = configMap.getOrDefault("apiKey", this.apiKey);
        this.baseUrl = configMap.getOrDefault("baseUrl", this.baseUrl);
        this.apiUrl = configMap.getOrDefault("apiUrl", this.apiUrl);
        this.uploadUrl = configMap.getOrDefault("uploadUrl", this.uploadUrl);
        this.prompt = configMap.getOrDefault("prompt", this.prompt);
        this.isTenant = configMap.getOrDefault("isTenant", this.isTenant);

        System.out.println("DifyConfig initialized: " + this);
    }
}
