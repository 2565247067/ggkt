package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Course;

import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vo.vod.CourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
public interface CourseService extends IService<Course> {

    Map<String, Object> findPageCourse(Page<Course> coursePage, CourseQueryVo courseQueryVo);

    /**
     * 添加课程基本信息
     * @param courseFormVo
     * @return
     */
    Long saveCourseInfo(CourseFormVo courseFormVo);

    CourseFormVo getCourseInfoById(Long id);

    void updateCourseInfoById(CourseFormVo courseFormVo);

    CoursePublishVo getCoursePublishVo(Long id);

    void publishCourseById(Long id);

    void removeCourseById(Long id);

    Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    Map<String, Object> getInfoById(Long courseId);
}
