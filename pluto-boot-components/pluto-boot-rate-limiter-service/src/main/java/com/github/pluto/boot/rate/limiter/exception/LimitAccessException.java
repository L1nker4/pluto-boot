package com.github.pluto.boot.rate.limiter.exception;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/26 20:13
 */
public class LimitAccessException extends RuntimeException {

    private final String errorMessage;

    public LimitAccessException(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
