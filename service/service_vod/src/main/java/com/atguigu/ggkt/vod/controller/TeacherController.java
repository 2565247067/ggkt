package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-05
 */
@RestController
@RequestMapping("admin/vod/teacher")
@AllArgsConstructor
@Api(tags = "讲师管理接口")

public class TeacherController {

    private final TeacherService teacherService;

    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")
    public Result<List<Teacher>> getAllTeacher(){
        //调用service方法
        List<Teacher> list = teacherService.list();
        return Result.Ok(list);
    }


    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public Result deleteByTeacherId(@ApiParam(name = "id",value = "ID",required = true)
                                         @PathVariable("id") Long id){
        boolean flag = teacherService.removeById(id);
        return flag ? Result.Ok() : Result.Error();
    }



    @ApiOperation("条件分页查询")
    @PostMapping ("findQueryPage/{current}/{limit}")
    public Result getPage(@ApiParam(name = "current",value = "当前页码") @PathVariable("current") Long current,
                          @ApiParam(name = "limit",value = "每页记录数") @PathVariable("limit") Long limit,
                          @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        //创建page分页对象
        Page<Teacher> page = new Page<>(current,limit);

        if(teacherQueryVo==null){
            List<Teacher> list = teacherService.list();
            return Result.Ok(list);
        }else{
            //获取条件值，进行非空判断
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String begin = teacherQueryVo.getJoinDateBegin();
            String end = teacherQueryVo.getJoinDateEnd();
            //进行封装
            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            if(!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }

            if(!StringUtils.isEmpty(level)){
                wrapper.like("level",level);
            }

            if(!StringUtils.isEmpty(begin)){
                wrapper.ge("join_date",begin);
            }

            if(!StringUtils.isEmpty(end)){
                wrapper.le("join_date",end);
            }

            //调用分页查询
            Page<Teacher> result = teacherService.page(page, wrapper);
            return Result.Ok(result);

        }
    }



    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result saveTeacher(@RequestBody Teacher teacher){
        boolean flag = teacherService.save(teacher);
        return flag ? Result.Ok() : Result.Error();
    }


    @ApiOperation("根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@ApiParam(name = "id",value = "老师id",required = true) @PathVariable("id") Long id){
        Teacher teacher = teacherService.getById(id);
        return  Result.Ok(teacher);
    }


    @ApiOperation("修改讲师")
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody Teacher teacher){
        boolean flag = teacherService.updateById(teacher);
        return flag ? Result.Ok() : Result.Error();
    }

    @ApiOperation("批量删除讲师")
    @DeleteMapping("removeBatch")
    public Result removeBatch(@ApiParam(name = "idList",value = "老师id集合")@RequestBody List<Long> idList){
        boolean flag = teacherService.removeByIds(idList);
        return flag ? Result.Ok() : Result.Error();
    }

}

