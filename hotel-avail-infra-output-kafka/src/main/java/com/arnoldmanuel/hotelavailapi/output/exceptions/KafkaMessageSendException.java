package com.arnoldmanuel.hotelavailapi.output.exceptions;

public class KafkaMessageSendException extends RuntimeException {

    public KafkaMessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
