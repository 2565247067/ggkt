package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.*;
import com.atguigu.ggkt.vo.vod.*;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
@Service
@AllArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private TeacherService teacherService;

    private SubjectService subjectService;

    private CourseDescriptionService courseDescriptionService;

    private ChapterService chapterService;

    private VideoService videoService;
    @Override
    public Map<String, Object> findPageCourse(Page<Course> coursePage, CourseQueryVo courseQueryVo) {

        //获取条件值
        String title = courseQueryVo.getTitle();
        Long subjectId = courseQueryVo.getSubjectId(); //第二层分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//第一层分类
        Long teacherId = courseQueryVo.getTeacherId();


        //判断条件是否为空
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)){
            queryWrapper.eq("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id",teacherId);
        }

        //调用方法实现条件查询分页
        Page<Course> pages = baseMapper.selectPage(coursePage, queryWrapper);

        //封装返回
        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数
        //每页数据集合
        List<Course> records = pages.getRecords();

        //获取id对应名称 并进行封装
        for (Course course :records) {
            //根据讲师id获取讲师名称
            Teacher teacher = teacherService.getById(course.getTeacherId());
            if(teacher!=null){
                String name = teacher.getName();
                course.getParam().put("teacherName",name);
            }

            //根据课程id获取课程名
            Subject idOne = subjectService.getById(course.getSubjectParentId());
            if(idOne!=null){
                course.getParam().put("subjectParentTitle",idOne.getTitle());
            }

            Subject idTwo = subjectService.getById(course.getSubjectId());
            if(idOne!=null){
                course.getParam().put("subjectTitle",idTwo.getTitle());
            }
        }


        //遍历封装讲师和分类名称
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);
        return map;
    }

    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //添加课程基本信息 操作course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.insert(course);

        //添加课程描述信息 操作course_description
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        //设置课程id
        courseDescription.setCourseId(course.getId());
        courseDescriptionService.save(courseDescription);
        return course.getId();
    }

    @Override
    public CourseFormVo getCourseInfoById(Long id) {
        //课程基本信息
        Course course = baseMapper.selectById(id);

        //如果基本信息不存在则直接返回null
        if(course==null) {
            return null;
        }

        //课程描述信息
        CourseDescription courseDescription = courseDescriptionService.getOne(
                new QueryWrapper<CourseDescription>().eq("course_id",id));
        //封装信息
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course,courseFormVo);

        //非空判断 为空则不填充
        if(courseDescription!=null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;

    }

    //修改课程信息，基本信息+描述信息
    @Override
    public void updateCourseInfoById(CourseFormVo courseFormVo) {
        //修改课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);

        //修改课程描述信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        //设置课程描述id
        courseDescription.setCourseId(course.getId());
       courseDescriptionService.update(courseDescription,new QueryWrapper<CourseDescription>()
               .eq("course_id",course.getId()));

    }

    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public void publishCourseById(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);
        course.setPublishTime(new Date());
        baseMapper.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCourseById(Long id) {

        //根据课程id 删除小节
        videoService.removeVideoByCourseId(id);
        //根据课程id 删除章节
        chapterService.removeByCourseId(id);
        //根据课程id 删除课程概述
        courseDescriptionService.removeByCourseId(id);
        //根据课程id 删除课程
        baseMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();//名称
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long teacherId = courseQueryVo.getTeacherId();//讲师
        //封装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }
        //调用方法查询
        Page<Course> pages = baseMapper.selectPage(pageParam, wrapper);

        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数

        //每页数据集合
        List<Course> records = pages.getRecords();
        
        records.stream().forEach(item->{
            this.getTeacherAndSubjectName(item);
        });

        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);

        return map;
    }

    private Course getTeacherAndSubjectName(Course course) {
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if(teacher != null) {
            course.getParam().put("teacherName",teacher.getName());
        }

        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;

    }

    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //课程浏览数量+1
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);

        //根据课程id查询详情
        Map<String, Object> map = new HashMap<>();
        CourseVo courseVo = baseMapper.selectCourseVoById(courseId);


        //查询章节、小节信息
        List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);

        //查询描述信息
        CourseDescription courseDescription = courseDescriptionService.getOne(
                new QueryWrapper<CourseDescription>().eq("course_id", courseId));


        //查询所属讲师信息
        Teacher teacher = teacherService.getById(course.getTeacherId());

        //封装map
        //TODO后续完善

        Boolean isBuy = false;

        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ?
                courseDescription.getDescription() : "");
        map.put("teacher", teacher);
        map.put("isBuy", isBuy);//是否购买
        return map;
    }
}
