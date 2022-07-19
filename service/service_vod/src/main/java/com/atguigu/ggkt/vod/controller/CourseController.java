package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
@RestController
@RequestMapping("admin/vod/course")
@Api(tags = "课程管理接口")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @ApiOperation("点播课程列表")
    @GetMapping("{page}/{limit}")
    public Result courseList(@PathVariable("page") Long page,
                             @PathVariable("limit") Long limit,
                             CourseQueryVo courseQueryVo){
        Page<Course> coursePage = new Page<>(page, limit);
        Map<String,Object> map = courseService.findPageCourse(coursePage,courseQueryVo);
        return Result.Ok(map);
    }

    @ApiOperation("添加课程基本信息")
    @PostMapping("save")
    public  Result save(@RequestBody CourseFormVo courseFormVo){
        Long courseId=courseService.saveCourseInfo(courseFormVo);
        return Result.Ok(courseId);
    }


    @ApiOperation("根据id获取课程信息")
    @GetMapping("get/{id}")
    public  Result save(@PathVariable("id") Long id){
        CourseFormVo courseFormVo=courseService.getCourseInfoById(id);
        return Result.Ok(courseFormVo);
    }

    @ApiOperation("修改课程基本信息")
    @PostMapping("update")
    public  Result update(@RequestBody CourseFormVo courseFormVo){
      courseService.updateCourseInfoById(courseFormVo);
        return Result.Ok(courseFormVo.getId());
    }


    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long id){

        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
        return Result.Ok(coursePublishVo);
    }

    @ApiOperation("根据id发布课程")
    @PutMapping("publishCourse/{id}")
    public Result publishCourseById(@ApiParam(value = "课程ID", required = true) @PathVariable("id") Long id){

        courseService.publishCourseById(id);
        return Result.Ok();
    }

    @ApiOperation("根据id删除课程")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") Long id){
        courseService.removeCourseById(id);
        return Result.Ok();
    }



}

