package com.symphony.logagent;

import com.symphony.logagent.service.LogInsightAgentService;
import com.symphony.logagent.repository.ErrorPatternRepository;
import com.symphony.logagent.repository.RemediationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromptBuilderTest {
    @InjectMocks
    private LogInsightAgentService promptBuilder;

    @Mock
    private ErrorPatternRepository errorRepo;

    @Mock
    private RemediationRepository remediationRepo;  // 🔥 MISSING PIECE

    //@Test
    /*void shouldBuildPromptWithAllErrorPatternsAndLog() {
        // given
        Map<String, String> patterns = Map.of(
                "DB_CONNECTION_ERROR", "Database connection timeout",
                "AUTH_FAILURE", "Invalid credentials"
        );

        when(errorRepo.getErrorPatterns()).thenReturn(patterns);

        String log = "ERROR: Connection timeout while accessing DB";

        // when
        String prompt = promptBuilder.buildPrompt(log);

        // then
        assertNotNull(prompt);

        // fixed instructions
        assertTrue(prompt.contains("You are a production support AI"));

        // error patterns
        assertTrue(prompt.contains("DB_CONNECTION_ERROR"));
        assertTrue(prompt.contains("Database connection timeout"));
        assertTrue(prompt.contains("AUTH_FAILURE"));

        // actual log
        assertTrue(prompt.contains(log));
    }*/
}
