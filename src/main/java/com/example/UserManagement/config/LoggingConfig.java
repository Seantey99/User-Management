package com.example.UserManagement.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Configuration
public class LoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    // Define the path to your logfile (customize as needed)
    private static final String LOG_FILE_PATH = "logs/usermanagement.log";

    /**
     * Truncates the existing logfile at application startup.
     * This avoids file-in-use errors that occur when trying to delete a log file
     * currently open by the logging framework.
     * <p>
     * For robust log management, prefer configuring log rotation and cleanup
     * in your logging framework (e.g., logback.xml, log4j2.xml).
     */
    @PostConstruct
    public void truncateExistingLogFile() {
        Path logPath = Paths.get(LOG_FILE_PATH);
        File logFile = logPath.toFile();
        if (logFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(logFile, false)) {
                // Opening with append=false truncates the file
                logger.info("Truncated existing logfile: {}", LOG_FILE_PATH);
            } catch (IOException e) {
                logger.error("Failed to truncate logfile {}: {}", LOG_FILE_PATH, e.getMessage());
            }
        } else {
            logger.info("No existing logfile found at: {}", LOG_FILE_PATH);
        }
    }

    /**
     * Logs application startup event.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationStartup() {
        logger.info("===============================================");
        logger.info("UserManagement Application Started at {}", Instant.now());
        logger.info("===============================================");
    }

    @Bean
    public WebFilter logFilter() {
        return (exchange, chain) -> {
            logRequest(exchange);
            return chain.filter(exchange)
                    .doOnSuccess(aVoid -> logResponse(exchange))
                    .doOnError(throwable -> logError(exchange, throwable));
        };
    }

    private void logRequest(ServerWebExchange exchange) {
        var request = exchange.getRequest();
        logger.info("[REQUEST] {} {} | Headers: {}", request.getMethod(), request.getURI(), request.getHeaders());
    }

    private void logResponse(ServerWebExchange exchange) {
        var response = exchange.getResponse();
        logger.info("[RESPONSE] {} {} | Status: {} | Headers: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                response.getStatusCode(),
                response.getHeaders());
    }

    private void logError(ServerWebExchange exchange, Throwable throwable) {
        logger.error("[ERROR] {} {} | Exception: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                throwable.getMessage(), throwable);
    }
}