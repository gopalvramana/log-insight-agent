INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('java.lang.NullPointerException', 'Accessing a null reference', 'Add null checks and validate inputs');
INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('java.util.concurrent.TimeoutException', 'Downstream service did not respond in time', 'Check downstream latency or increase timeout');
INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('java.sql.SQLException', 'Database access error', 'Check database connection and query syntax');
INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('IOException', 'Input/output operation failed', 'Check file paths and network connections');
INSERT INTO error_knowledge (error_key, description, remediation) VALUES ('AuthenticationException', 'User authentication failed', 'Verify user credentials and authentication service status');

