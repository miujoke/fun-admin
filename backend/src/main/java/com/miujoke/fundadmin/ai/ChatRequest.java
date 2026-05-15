package com.miujoke.fundadmin.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {

    @NotBlank(message = "提问内容不能为空")
    private String question;
}