package com.symphony.logagent.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Data
public class LogAnalysisRequest {
    String logs;
    String service;
    String environment;
}
