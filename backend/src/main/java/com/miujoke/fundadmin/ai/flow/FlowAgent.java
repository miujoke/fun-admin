package com.miujoke.fundadmin.ai.flow;

import com.miujoke.fundadmin.ai.chat.ChatState;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * @author miujoke
 * @version 1.0.0
 * <p>
 * class description goes here
 * description:
 * @date 2026/5/18 14:06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlowAgent {

    private CompiledGraph<FlowState> flowCompiledGraph;

    @PostConstruct
    public void init() throws Exception {
        // Define the graph structure
        flowCompiledGraph = new StateGraph<>(FlowState.SCHEMA, initData -> new FlowState(initData))
                .addNode("flow", node_async(new FlowNode()))
                .addNode("responder", node_async(new ResponderNode()))
                // Define edges
                .addEdge(START, "flow") // Start with the greeter node
                .addEdge("flow", "responder")
                .addEdge("responder", END).compile()   // End after the responder node
        ;
        // Compile the graph


        // Run the graph
        // The `stream` method returns an AsyncGenerator.
        // For simplicity, we'll collect results. In a real app, you might process them as they arrive.
        // Here, the final state after execution is the item of interest.

        for (var item : flowCompiledGraph.stream(Map.of(FlowState.MESSAGES_KEY, "Let's, begin!"))) {

            System.out.println(item);
        }
    }

    public FlowState flow() throws Exception {
        Optional<FlowState> result = flowCompiledGraph.invoke(Map.of(FlowState.MESSAGES_KEY, "Let's, begin!"));
        return result.orElseThrow(() -> new RuntimeException("Workflow execution returned no result"));
    }

}
