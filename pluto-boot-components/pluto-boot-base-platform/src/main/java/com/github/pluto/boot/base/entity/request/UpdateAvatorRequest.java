package com.github.pluto.boot.base.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author ：L1nker4
 * @date ： 创建于  2020/7/29 19:44
 * @description： 更新头像 请求类
 */

@Data
public class UpdateAvatorRequest {

    @NotBlank(message = "{required}")
    private String username;

    @NotBlank(message = "{required}")
    private String avatar;
}
