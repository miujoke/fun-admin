package com.miujoke.fundadmin.ai;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleChatAgent {

    private final ChatClient chatClient;
    private CompiledGraph<ChatState> workflow;

    @PostConstruct
    public void init() throws Exception {
        workflow = new StateGraph<>(ChatState.channels(), ChatState::new)
                .addNode("chat", AsyncNodeAction.node_async((NodeAction<ChatState>) state -> {
                    String question = state.getQuestion();
                    log.info("LangGraph chat node processing: {}", question);
                    String answer = chatClient.prompt()
                            .user(question)
                            .call()
                            .content();
                    return Map.of(ChatState.ANSWER, answer);
                }))
                .addEdge(StateGraph.START, "chat")
                .addEdge("chat", StateGraph.END)
                .compile();
        log.info("LangGraph workflow compiled successfully");
    }

    public ChatState chat(String question) throws Exception {
        Optional<ChatState> result = workflow.invoke(Map.of(ChatState.QUESTION, question));
        return result.orElseThrow(() -> new RuntimeException("Workflow execution returned no result"));
    }
}