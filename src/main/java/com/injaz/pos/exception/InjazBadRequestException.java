package com.injaz.pos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InjazBadRequestException extends RuntimeException {

    public InjazBadRequestException(String message) {
        super(message);
    }

    public InjazBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
