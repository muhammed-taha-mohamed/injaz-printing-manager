package com.injaz.pos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InjazAppException extends RuntimeException {
	
    public InjazAppException(String message) {
        super(message);
    }

    public InjazAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
