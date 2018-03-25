package com.incdocs.config.interceptor;

import com.incdocs.model.response.ErrorResponse;
import com.incdocs.utils.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class FaultHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(value = { ApplicationException.class })
    protected ResponseEntity<Object> handleApplicationExceptions(ApplicationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage());
        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), ex.getHttpStatusCode(), request);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage());

        return handleExceptionInternal(ex, errorResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
