package com.symphony.logagent.builder;

import java.util.List;
import java.util.StringJoiner;

public class AgentPromptBuilder {
    private String systemMessage;
    private String taskInstruction;
    private String inputPayload;
    private String outputFormat;

    private final StringJoiner knowledgeContext = new StringJoiner("\n", "", "");

    // -------- SYSTEM --------
    public AgentPromptBuilder system(String message) {
        this.systemMessage = message;
        return this;
    }

    // -------- TASK --------
    public AgentPromptBuilder task(String instruction) {
        this.taskInstruction = instruction;
        return this;
    }

    // -------- KNOWLEDGE CONTEXT --------
    public AgentPromptBuilder addKnowledge(String title, List<String> entries) {
        if (entries == null || entries.isEmpty()) return this;

        knowledgeContext.add(title + ":");
        entries.forEach(e -> knowledgeContext.add("- " + e));
        knowledgeContext.add("");
        return this;
    }

    // -------- INPUT --------
    public AgentPromptBuilder input(String payload) {
        this.inputPayload = payload;
        return this;
    }

    // -------- OUTPUT RULES --------
    public AgentPromptBuilder outputFormat(String format) {
        this.outputFormat = format;
        return this;
    }

    // -------- FINAL PROMPT --------
    public String build() {
        StringBuilder prompt = new StringBuilder();

        appendSection(prompt, "SYSTEM", systemMessage);
        appendSection(prompt, "TASK", taskInstruction);
        appendSection(prompt, "CONTEXT", knowledgeContext.toString());
        appendSection(prompt, "INPUT", inputPayload);
        appendSection(prompt, "OUTPUT FORMAT", outputFormat);

        return prompt.toString();
    }

    private void appendSection(StringBuilder sb, String title, String content) {
        if (content == null || content.isBlank()) return;

        sb.append("### ").append(title).append("\n");
        sb.append(content).append("\n\n");
    }


}
