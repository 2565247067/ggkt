package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.VideoVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@RestController
@RequestMapping("admin/vod/videoVisitor")
@Api(tags = "课程观看人数接口")

@AllArgsConstructor
public class VideoVisitorController {
    private VideoVisitorService videoVisitorService;

    @ApiOperation("课程统计接口")
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCoount(@PathVariable("courseId") Long courseId,
                             @PathVariable("startDate") String startDate,
                             @PathVariable("endDate") String endDate){
        Map<String,Object> map= videoVisitorService.findCount(courseId,startDate,endDate);
        return Result.Ok(map);
    }
}

