package br.com.rplbasicmessages.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RplMessageNotFoundException extends AbstractRplMessageException {

    public RplMessageNotFoundException(String message, Class<?> local) {
        super(message, local);
    }

    public RplMessageNotFoundException(String message, Throwable cause, Class<?> local) {
        super(message, cause, local);
    }
}
