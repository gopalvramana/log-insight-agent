package com.symphony.logagent.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.http.*;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class KafkaToRestConsumer {

    private static final String REST_URL = "http://localhost:8080/agent/log/analyze"; // your REST endpoint
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "log-insight-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("app-logs"));

        HttpClient httpClient = HttpClient.newHttpClient();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            log.info("Polled {} records", records.count());
            for (ConsumerRecord<String, String> record : records) {

                String rawLog = record.value();
                log.info("Processing log from service {}: {}", record.key(), rawLog);

                if (!rawLog.contains("Exception") && !rawLog.contains("Error")) {
                    continue; // skip INFO/WARN
                }

                // Build JSON payload
                try {
                    String jsonPayload = mapper.writeValueAsString(new LogPayload(
                            summarize(rawLog),
                            record.key() != null ? record.key() : "unknown-service",
                            "prod"
                    ));

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(REST_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("REST Response: " + response.body());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Simple summary extractor: pick first line or key info
    private static String summarize(String log) {
        String[] lines = log.split("\n");
        return lines[0]; // e.g., "NullPointerException at OrderService.java:52"
    }

    // POJO for JSON payload
    public static class LogPayload {
        public String logs;
        public String service;
        public String environment;

        public LogPayload(String logs, String service, String environment) {
            this.logs = logs;
            this.service = service;
            this.environment = environment;
        }
    }
}

