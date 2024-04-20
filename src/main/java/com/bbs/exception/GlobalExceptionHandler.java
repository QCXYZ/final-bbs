package com.bbs.exception;

import com.bbs.util.R;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R<?> handleExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            e.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                response.put(fieldName, errorMessage);
            });
            return R.fail(response);
        } else if (ex instanceof NullPointerException) {
            response.put("message", "空指针异常");
            return R.fail(response);


        } else if (ex instanceof ResourceNotFoundException) {
            response.put("message", ex.getMessage());
            return R.fail(response);


        } else if (ex instanceof RuntimeException) {
            response.put("message", ex.getMessage());
            return R.fail(response);
        } else {  // 通用异常处理
            response.put("message", ex.getMessage());
            return R.fail(response);
        }
    }
}
