package com.miujoke.fundadmin.ai.flow;

import com.miujoke.fundadmin.ai.chat.ChatRequest;
import com.miujoke.fundadmin.ai.chat.ChatState;
import com.miujoke.fundadmin.ai.chat.SimpleChatAgent;
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

/**
 * @author miujoke
 * @version 1.0.0
 * <p>
 * class description goes here
 * description:
 * @date 2026/5/18 15:34
 */

@Slf4j
@Tag(name = "AI 对话")
@RestController
@RequestMapping("/api/ai/flow")
@RequiredArgsConstructor
public class FlowController {

    private final FlowAgent flowAgent;

    @Operation(summary = "自定义工作流", description = "自定义 LangGraph4J 工作流")
    @PostMapping
    public Result<Map<String, Object>> chat(@Valid @RequestBody ChatRequest request) {
        try {
            FlowState state = flowAgent.flow();
            return Result.success(Map.of(
                    "messages", state.messages()
            ));
        } catch (Exception e) {
            log.error("cust flow failed: {}", e.getMessage(), e);
            return Result.fail("自定义流程失败: " + e.getMessage());
        }
    }
}
