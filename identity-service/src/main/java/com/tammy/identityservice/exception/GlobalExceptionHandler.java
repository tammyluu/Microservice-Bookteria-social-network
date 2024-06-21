package com.tammy.identityservice.exception;

import com.tammy.identityservice.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;

import java.nio.file.AccessDeniedException;


@ControllerAdvice
public class GlobalExceptionHandler {
    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }
    // Response by String message
    /*@ExceptionHandler( value = RuntimeException.class)
    ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }*/

 /*   @ExceptionHandler( value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(1001);
        apiResponse.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }*/

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
    // Exception for AccessDeniedException
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        // return response entity with body (status, code, message)
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        //In service , we throw AppException et load error code, now we get  this error code
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        // error code has 2 properties (code, message)
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e){

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
