package com.miujoke.fundadmin.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AI 配置
 */
@Configuration
public class AiConfig {

    /**
     * 配置 ChatClient
     * 需要在 application.yml 中配置 openai.api-key
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一位专业的基金投资顾问，熟悉各类基金产品和投资策略。")
                .build();
    }
}