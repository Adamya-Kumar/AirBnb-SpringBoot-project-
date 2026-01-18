package com.codingshuttle.project.airBnb.advice;


import com.codingshuttle.project.airBnb.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(com.codingshuttle.project.airBnb.exceptions.ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        ApiError error= ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage().toString())
                .build();
        return buildApiResponseWithResponseEntity(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternlServerError(Exception exception){
        ApiError apiError=ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return buildApiResponseWithResponseEntity(apiError);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<String> errors=exception.
                getBindingResult().
                getAllErrors().
                stream().
                map(error->error.getDefaultMessage()).
                collect(Collectors.toList());

        ApiError apiError=ApiError.builder()
                .status(HttpStatus.BAD_GATEWAY)

                .message("Input Validation failed")
                .subError(errors)
                .build();
        return buildApiResponseWithResponseEntity(apiError);
    }

    private ResponseEntity<ApiResponse<?>> buildApiResponseWithResponseEntity(ApiError error) {
        return new ResponseEntity<>(new ApiResponse<>(error),error.getStatus());
    }
}
