package org.shu.yzy.seckillidea.exception;

import org.shu.yzy.seckillidea.Enum.ResultEnum;
import org.shu.yzy.seckillidea.vo.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> globalExceptionHandler(GlobalException e) {
        return Result.getResult(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public Result<String> BindExceptionHandler(BindException e){
        return Result.getResult(e.getErrorCount(), e.getMessage());
    }
}
