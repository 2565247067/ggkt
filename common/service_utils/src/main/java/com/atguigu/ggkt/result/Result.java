package com.atguigu.ggkt.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:同意返回结果类
 * @author: 25652
 * @time: 2022/7/13 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "全局统一返回结果")
public class Result<T> {

    /**
     * 状态码
     */
    @ApiModelProperty(value = "返回码")
    private Integer code;

    /**
     * 返回状态消息 成功、失败
     */
    @ApiModelProperty(value = "返回状态消息")
    private String message;

    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的数据")
    private T data;

    /**
     * 成功的方法 无数据返回
     * @param <T>
     * @return
     */
    public static<T> Result<T> Ok(){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    /**
     * 成功的方法 有数据返回
     * @param <T>
     * @return
     */
    public static<T> Result<T> Ok(T data){
        Result<T> result = new Result<>();
        if(data!=null) {
            result.setData(data);
        }
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    /**
     * 失败的方法 无数据返回
     * @param <T>
     * @return
     */
    public static<T> Result<T> Error(){
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMessage("失败");
        return result;
    }


    /**
     * 失败的方法 有数据返回
     * @param <T>
     * @return
     */
    public static<T> Result<T> Error(T data){
        Result<T> result = new Result<>();
        if(data!=null) {
            result.setData(data);
        }
        result.setCode(201);
        result.setMessage("失败");
        return result;
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
