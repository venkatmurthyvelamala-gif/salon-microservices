package com.user.exception;

import com.user.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception ex,
                                                              WebRequest req){
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


    }

}
