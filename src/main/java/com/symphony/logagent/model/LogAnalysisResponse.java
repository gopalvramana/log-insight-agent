package com.symphony.logagent.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class LogAnalysisResponse {
    String errorType;
    String rootCause;
    String confidence;
    String suggestedAction;
    String explanation;
}
