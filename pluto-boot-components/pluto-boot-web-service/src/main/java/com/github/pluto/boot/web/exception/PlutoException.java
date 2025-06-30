package com.github.pluto.boot.web.exception;

import lombok.NoArgsConstructor;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/24 21:06
 * @description： 自定义异常
 */
@NoArgsConstructor
public class PlutoException extends RuntimeException{

    public PlutoException(String message) {
        super(message);
    }
}
