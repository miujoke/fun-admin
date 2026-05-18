package com.miujoke.fundadmin.ai.flow;

import org.bsc.langgraph4j.action.NodeAction;

import java.util.List;
import java.util.Map;

/**
 * @author miujoke
 * @version 1.0.0
 * <p>
 * class description goes here
 * description:
 * @date 2026/5/18 14:05
 */
public class ResponderNode implements NodeAction<FlowState> {
    @Override
    public Map<String, Object> apply(FlowState state) {
        List<String> currentMessages = state.messages();
        if (currentMessages.contains("Hello from GreeterNode!")) {
            return Map.of(FlowState.MESSAGES_KEY, "Acknowledged greeting!");
        }
        return Map.of(FlowState.MESSAGES_KEY, "No greeting found.");
    }
}
