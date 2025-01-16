package br.com.rplbasicmessages.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RplMessageUnauthorizedException extends AbstractRplMessageException {

    public RplMessageUnauthorizedException(String message, Class<?> local) {
        super(message, local);
    }

    public RplMessageUnauthorizedException(String message, Throwable cause, Class<?> local) {
        super(message, cause, local);
    }
}
