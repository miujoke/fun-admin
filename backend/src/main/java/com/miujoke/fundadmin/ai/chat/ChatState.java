package com.miujoke.fundadmin.ai.chat;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.Map;

public class ChatState extends AgentState {

    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String IS_SAFE = "isSafe"; // 给 check 节点用的字段


    public static Map<String, Channel<?>> channels() {
        return Map.of(
                QUESTION, Channels.base(() -> ""),
                ANSWER, Channels.base(() -> ""),
                IS_SAFE, Channels.base(() -> "0")
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

    public String  getIsSafe() {
        return value(IS_SAFE).map(Object::toString).orElse("0");
    }

}