package com.github.pluto.boot.base.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author ：L1nker4
 * @date ： 创建于  2020/7/22 17:02
 * @description： 登录 请求实体类
 */

@Data
public class LoginUserRequest {

    @NotBlank(message = "{required}")
    private String username;

    @NotBlank(message = "{required}")
    private String password;

    @NotBlank(message = "{required}")
    private String uuid;

    @NotBlank(message = "{required}")
    private String code;
}
