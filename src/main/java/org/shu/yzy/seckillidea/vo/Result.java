package org.shu.yzy.seckillidea.vo;

import lombok.Data;
import org.shu.yzy.seckillidea.Enum.ResultEnum;

@Data
public class Result<T> {
    private int code;

    private String msg;

    private T data;

    public static  <T> Result<T> getResult(ResultEnum resultEnum, T data){
        return new Result<T>(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public static  <T> Result<T> getResult(ResultEnum resultEnum){
        return new Result<T>(resultEnum.getCode(), resultEnum.getMsg());
    }

    public static <T> Result<T> getResult(int code, String msg){
        return new Result<T>(code, msg);
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
