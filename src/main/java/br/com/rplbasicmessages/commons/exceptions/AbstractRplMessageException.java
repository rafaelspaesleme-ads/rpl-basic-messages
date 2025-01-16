package br.com.rplbasicmessages.commons.exceptions;


import java.time.LocalDateTime;

public abstract class AbstractRplMessageException extends RuntimeException {

    protected String local;
    protected LocalDateTime timestamp;

    protected AbstractRplMessageException() {
    }

    protected AbstractRplMessageException(String message, Class<?> local) {
        super(message);
        this.local = local.getSimpleName();
        this.timestamp = LocalDateTime.now();
    }

    protected AbstractRplMessageException(String message, Throwable cause, Class<?> local) {
        super(message, cause);
        this.local = local.getSimpleName();
        this.timestamp = LocalDateTime.now();
    }
}
