package com.atguigu.ggkt.order.controller;


import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-19
 */
@RestController
@RequestMapping("admin/order/orderInfo")
@Api(tags = "订单管理")
@AllArgsConstructor
public class OrderInfoController {

    private OrderInfoService orderInfoService;

    @ApiOperation("获取订单列表")
    @GetMapping("{page}/{limit}")
    public Result listOrder(@PathVariable("page") Long page,
                            @PathVariable("limit") Long limit,
                            OrderInfoQueryVo orderInfoQueryVo){
        //创建page对象
        Page<OrderInfo> pageParam = new Page<>(page, limit);

       Map<String,Object> map = orderInfoService.selectOrderInfoPage(pageParam,orderInfoQueryVo);
       return Result.Ok(map);
    }
}

