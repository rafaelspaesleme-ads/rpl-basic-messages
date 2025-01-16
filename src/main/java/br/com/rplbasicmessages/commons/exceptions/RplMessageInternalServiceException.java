package br.com.rplbasicmessages.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RplMessageInternalServiceException extends AbstractRplMessageException {

    public RplMessageInternalServiceException(String message, Class<?> local) {
        super(message, local);
    }

    public RplMessageInternalServiceException(String message, Throwable cause, Class<?> local) {
        super(message, cause, local);
    }
}
