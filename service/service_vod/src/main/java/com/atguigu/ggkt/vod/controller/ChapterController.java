package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vod.service.ChapterService;
import com.atguigu.ggkt.vod.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
@RestController
@RequestMapping("admin/vod/chapter")
//@CrossOrigin
@AllArgsConstructor
@Api(tags = "课程大纲接口")
public class ChapterController {

    private ChapterService chapterService;

    private VideoService videoService;

    @ApiOperation("嵌套章节数据列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable("courseId") Long courseId){
       List<ChapterVo> list= chapterService.getTreeList(courseId);
       return Result.Ok(list);
    }


    @ApiOperation("添加章节")
    @PostMapping("save")
    public  Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.Ok();
    }


    @ApiOperation("根据id查询章节")
    @GetMapping("get/{id}")
    public  Result save(@PathVariable("id") Long id){
        Chapter chapter = chapterService.getById(id);
        return Result.Ok(chapter);
    }


    @ApiOperation("修改章节")
    @PostMapping("update")
    public  Result update(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.Ok();
    }


    @ApiOperation("根据id删除章节")
    @DeleteMapping("remove/{id}")
    public  Result remove(@PathVariable("id") Long id){
        chapterService.removeById(id);
        videoService.removeVideoByChapterId(id);
        return Result.Ok();
    }
}

