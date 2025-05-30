package com.github.pluto.boot.base.common.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Vue路由 Meta
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouterMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 5499925008927195914L;

    private Boolean closeable;

    private Boolean isShow;

}
