package br.com.copa.discoveryserver.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PapertrailHttpAppender extends AppenderBase<ILoggingEvent> {

    private String url;
    private String token;
    private String appName;
    private HttpClient httpClient;
    private ExecutorService executor;
    private boolean enabled;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public void start() {
        if (isBlank(url) || isBlank(token)) {
            enabled = false;
            super.start();
            return;
        }

        enabled = true;
        executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "papertrail-http-appender");
            thread.setDaemon(true);
            return thread;
        });
        httpClient = HttpClient.newBuilder().executor(executor).build();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted() || !enabled) {
            return;
        }

        event.prepareForDeferredProcessing();
        String payload = format(event);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .exceptionally(exception -> {
                    addWarn("Failed to send log to Papertrail.", exception);
                    return null;
                });
    }

    @Override
    public void stop() {
        super.stop();
        if (executor != null) {
            executor.shutdown();
        }
    }

    private String format(ILoggingEvent event) {
        StringBuilder line = new StringBuilder()
                .append(Instant.ofEpochMilli(event.getTimeStamp()))
                .append(' ')
                .append(Objects.toString(appName, "application"))
                .append(' ')
                .append(level(event.getLevel()))
                .append(" [")
                .append(event.getThreadName())
                .append("] ")
                .append(event.getLoggerName())
                .append(" - ")
                .append(event.getFormattedMessage());

        if (event.getThrowableProxy() != null) {
            line.append(System.lineSeparator()).append(ThrowableProxyUtil.asString(event.getThrowableProxy()));
        }

        return line.append(System.lineSeparator()).toString();
    }

    private String level(Level level) {
        return level == null ? "INFO" : level.toString();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank() || value.endsWith("_IS_UNDEFINED");
    }
}
