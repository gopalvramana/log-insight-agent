
-- Create table only if it does not exist
CREATE TABLE IF NOT EXISTS error_knowledge (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    error_key VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    remediation VARCHAR(1000)
);

-- Insert initial knowledge entries
INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('NullPointerException', 'Accessing a null reference', 'Add null checks and validate inputs');
INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('TimeoutException', 'Downstream service did not respond in time', 'Check downstream latency or increase timeout');

