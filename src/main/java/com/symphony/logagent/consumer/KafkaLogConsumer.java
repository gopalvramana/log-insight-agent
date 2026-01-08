package com.symphony.logagent.consumer;

import com.symphony.logagent.DTO.LogInsightRequest;
import com.symphony.logagent.model.LogInsightResponse;
import com.symphony.logagent.service.LogInsightAgentService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class KafkaLogConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final LogInsightAgentService agent;

    public KafkaLogConsumer(KafkaConsumer<String, String> consumer,
                            LogInsightAgentService agent) {
        this.consumer = consumer;
        this.agent = agent;
    }

    @PostConstruct
    public void start() {
        Thread consumerThread = new Thread(this::pollLoop);
        consumerThread.setName("kafka-log-consumer");
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    private void pollLoop() {
        log.info("KafkaLogConsumer started.....");
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(500));

            log.info("Polled {} records", records.count());
            for (ConsumerRecord<String, String> record : records) {
                log.info("Processing log from service {}: {}", record.key(), record.value());
                process(record);
            }
        }
    }

    private void process(ConsumerRecord<String, String> record) {
        String rawLog = record.value();
        log.info("Processing log: {}", rawLog);

        // Skip noise
        if (!isException(rawLog)) {
            return;
        }       

        LogInsightRequest request = new LogInsightRequest(
                summarize(rawLog),
                record.key() != null ? record.key() : "unknown-service",
                "prod"
        );
        log.info("Built LogInsightRequest: {}", request);
        try {
            LogInsightResponse response = agent.analyze(String.valueOf(request));
            System.out.println("Agent response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isException(String log) {
        return log.contains("Exception") || log.contains("ERROR");
    }

    private String summarize(String log) {
        return log.split("\n")[0];
    }
}

