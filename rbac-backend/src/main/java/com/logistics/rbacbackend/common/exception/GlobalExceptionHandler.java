package com.logistics.rbacbackend.common.exception;

import com.logistics.rbacbackend.common.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-13:51
 * @description com.logistics.rbacbackend.common.exception
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 参数校验：@Valid + @RequestBody DTO 触发
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ApiResponse.fail(400, msg.isBlank() ? "参数校验失败" : msg);
    }

    /**
     * 参数校验：表单绑定/QueryParam 绑定失败
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBindException(BindException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ApiResponse.fail(400, msg.isBlank() ? "参数绑定失败" : msg);
    }

    /**
     * 参数校验：@Validated + @RequestParam 触发
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraintViolation(ConstraintViolationException e) {
        return ApiResponse.fail(400, e.getMessage());
    }

    /**
     * 你在 Service 里常用的业务异常（你之前就经常 throw IllegalArgumentException）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.fail(400, e.getMessage());
    }

    /**
     * 兜底：未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        // 生产环境建议不要直接把异常信息返回给前端，这里为了实训方便
        return ApiResponse.fail(500, "服务器内部错误: " + e.getMessage());
    }
}
