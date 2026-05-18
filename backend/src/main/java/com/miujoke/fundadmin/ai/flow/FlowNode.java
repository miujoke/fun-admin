package com.miujoke.fundadmin.ai.flow;

import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

/**
 * @author miujoke
 * @version 1.0.0
 * <p>
 * class description goes here
 * description:
 * @date 2026/5/18 14:04
 */
public class FlowNode implements NodeAction<FlowState> {
    @Override
    public Map<String, Object> apply(FlowState state) {
        System.out.println("GreeterNode executing. Current messages: " + state.messages());
        return Map.of(FlowState.MESSAGES_KEY, "Hello from GreeterNode!");
    }
}
