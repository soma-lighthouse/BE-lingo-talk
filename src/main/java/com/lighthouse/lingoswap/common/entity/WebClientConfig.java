package com.lighthouse.lingoswap.common.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile({"local", "dev"})
public class WebClientConfig { ///센드버드컨피그로 이름을 바꿔야함

    @Value("${sendbird.api-url}")
    private String apiUrl;

    @Value("${sendbird.api-token}")
    private String apiToken;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Content-Type", "application/json; charset=utf8")
                .defaultHeader("Api-Token", apiToken)
                .build();
    }
}