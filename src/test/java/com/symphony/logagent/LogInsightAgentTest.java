package com.symphony.logagent;

import com.symphony.logagent.service.LLMAgentLogAnalyzerAgent;
import com.symphony.logagent.model.LogInsightResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogInsightAgentTest {
    @Mock
    private LLMAgentLogAnalyzerAgent promptBuilder;

    @Mock
    private ChatModel chatModel;

    @Test
    void shouldCallLLMWithBuiltPrompt() {

        String log = "NullPointerException at OrderService.java:52";
        String prompt = "PROMPT_FOR_NPE";
        String llmResponse = "This is a NullPointerException caused by null object";

        // Arrange
        when(promptBuilder.buildPrompt(log)).thenReturn(prompt);
        when(chatModel.call(prompt)).thenReturn(llmResponse);

        // Act
        LogInsightResponse result = promptBuilder.analyze(log);

        // Assert
        assertEquals(llmResponse, result);

        verify(promptBuilder).buildPrompt(log);
        verify(chatModel).call(prompt);
    }
}
