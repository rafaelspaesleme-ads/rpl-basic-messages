package br.com.rplbasicmessages.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RplMessageBusinessException extends AbstractRplMessageException {

    public RplMessageBusinessException(String message, Class<?> local) {
        super(message, local);
    }

    public RplMessageBusinessException(String message, Throwable cause, Class<?> local) {
        super(message, cause, local);
    }
}
