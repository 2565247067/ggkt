package com.atguigu.ggkt.exception;

/**
 * @description:统一异常处理类
 * @author: 25652
 * @time: 2022/7/13 20:48
 */

import com.atguigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.Error().message("服务器异常，请稍后再试");
    }
}