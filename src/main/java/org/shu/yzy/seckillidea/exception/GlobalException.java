package org.shu.yzy.seckillidea.exception;

import lombok.Data;
import org.shu.yzy.seckillidea.Enum.ResultEnum;

@Data
public class GlobalException extends RuntimeException{

    private Integer code;

    public GlobalException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
