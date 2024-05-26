package com.github.pluto.boot.web.exception.handler;

import com.github.pluto.boot.web.entity.CommonResult;
import com.github.pluto.boot.web.exception.PlutoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2024/05/24 20:22
 * @description：    全局异常处理类
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {



    @ExceptionHandler(value = PlutoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult handleParamsInvalidException(PlutoException e) {
        log.error("系统错误：{}", e.getMessage());
        return new CommonResult().failed(e.getMessage());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return CommonResult
     */
    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new CommonResult().validateFailed(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}
