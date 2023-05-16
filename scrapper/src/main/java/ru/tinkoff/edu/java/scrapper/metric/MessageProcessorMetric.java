package ru.tinkoff.edu.java.scrapper.metric;

import io.micrometer.core.instrument.Metrics;

public final class MessageProcessorMetric {

    private MessageProcessorMetric() {
    }

    public static void incrementProcessedMessageCount() {
        Metrics.counter("processed_messages_count").increment();
    }
}
