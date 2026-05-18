# LangGraph4J 简单对话示例说明

## 工作流程

```
┌──────────┐     ┌─────────────┐     ┌──────────┐
│  START   │────▶│    chat     │────▶│   END    │
└──────────┘     │ (调用大模型) │     └──────────┘
                 └─────────────┘
```

用户提问通过 StateGraph 流入 chat 节点，节点调用 Spring AI ChatClient 与大模型对话，结果写入状态返回。

## 核心组件

| 文件 | 说明 |
|------|------|
| `ChatRequest` | 请求 DTO，接收用户提问（`question`） |
| `ChatState` | LangGraph4J 状态对象，定义 `question` 和 `answer` 两个 Channel，在节点间传递数据 |
| `SimpleChatAgent` | 工作流定义与执行，构建 StateGraph 并编译为 CompiledGraph |
| `ChatController` | REST 接口，`POST /api/ai/chat` |

## 关键代码解析

### 1. ChatState — 状态定义

状态通过 Channel 定义数据的读写规则：

```java
public static Map<String, Channel<?>> channels() {
    return Map.of(
        "question", Channels.base(() -> ""),   // 基础 channel，存储用户提问
        "answer",   Channels.base(() -> "")    // 基础 channel，存储大模型回答
    );
}
```

`Channels.base()` 表示每次写入直接替换旧值（不像 `appender` 会追加到列表）。

### 2. SimpleChatAgent — 构建工作流

```java
workflow = new StateGraph<>(ChatState.channels(), ChatState::new)
    .addNode("chat", AsyncNodeAction.node_async((NodeAction<ChatState>) state -> {
        String question = state.getQuestion();
        String answer = chatClient.prompt().user(question).call().content();
        return Map.of(ChatState.ANSWER, answer);
    }))
    .addEdge(StateGraph.START, "chat")
    .addEdge("chat", StateGraph.END)
    .compile();
```

要点：
- **`StateGraph` 构造**：传入 channels 定义和状态工厂（`ChatState::new`）
- **`addNode`**：用 `AsyncNodeAction.node_async()` 将同步 NodeAction 包装为异步，解决 addNode 方法重载歧义
- **节点逻辑**：从 state 取 question → 调用 ChatClient → 返回 Map 更新 answer channel
- **`addEdge`**：定义流转顺序，START → chat → END

### 3. 执行工作流

```java
Optional<ChatState> result = workflow.invoke(Map.of("question", "你好"));
String answer = result.get().getAnswer();
```

`invoke` 接收初始状态数据（Map），返回最终状态的 Optional。

### 4. ChatController — REST 接口

```java
@PostMapping
public Result<Map<String, String>> chat(@Valid @RequestBody ChatRequest request) {
    ChatState state = simpleChatAgent.chat(request.getQuestion());
    return Result.success(Map.of("question", state.getQuestion(), "answer", state.getAnswer()));
}
```

接口路径 `/api/ai/chat`，无需登录（SecurityConfig 白名单配置了 `/api/ai/**`）。

## 调用示例

```bash
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"question": "你好，请介绍一下你自己"}'
```

响应：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "question": "你好，请介绍一下你自己",
    "answer": "你好！我是一个AI助手..."
  }
}
```

也可在 Knife4j 文档页面（`/doc.html`）中直接测试。

## 扩展方向

当前是单节点示例，后续可扩展为多节点工作流：

- 添加条件分支（`addConditionalEdges`）实现不同路径
- 添加多个节点（如数据分析 → 风险评估 → 建议生成）
- 使用 `Channels.appender()` 在节点间累积消息列表
- 引入人机交互节点（interrupt/human review）