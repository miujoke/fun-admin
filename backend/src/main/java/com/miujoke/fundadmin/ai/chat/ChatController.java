package com.miujoke.fundadmin.ai.chat;

import com.miujoke.fundadmin.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Tag(name = "AI 对话")
@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpleChatAgent simpleChatAgent;

    @Operation(summary = "简单对话", description = "通过 LangGraph4J 工作流调用大模型进行对话")
    @PostMapping
    public Result<Map<String, String>> chat(@Valid @RequestBody ChatRequest request) {
        try {
            ChatState state = simpleChatAgent.chat(request.getQuestion());
            return Result.success(Map.of(
                    "question", state.getQuestion(),
                    "answer", state.getAnswer()
            ));
        } catch (Exception e) {
            log.error("AI chat failed: {}", e.getMessage(), e);
            return Result.fail("AI 对话失败: " + e.getMessage());
        }
    }
}