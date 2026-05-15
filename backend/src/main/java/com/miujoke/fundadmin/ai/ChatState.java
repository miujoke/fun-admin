package com.miujoke.fundadmin.ai;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.Map;

public class ChatState extends AgentState {

    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";

    public static Map<String, Channel<?>> channels() {
        return Map.of(
                QUESTION, Channels.base(() -> ""),
                ANSWER, Channels.base(() -> "")
        );
    }

    public ChatState(Map<String, Object> initData) {
        super(initData);
    }

    public String getQuestion() {
        return value(QUESTION).map(Object::toString).orElse("");
    }

    public String getAnswer() {
        return value(ANSWER).map(Object::toString).orElse("");
    }
}