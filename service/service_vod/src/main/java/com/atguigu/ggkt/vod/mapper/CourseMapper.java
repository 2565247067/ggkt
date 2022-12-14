package com.atguigu.ggkt.vod.mapper;

import com.atguigu.ggkt.model.vod.Course;

import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
public interface CourseMapper extends BaseMapper<Course> {

    CoursePublishVo selectCoursePublishVoById(@Param("id") Long id);

    CourseVo selectCourseVoById(@Param("courseId") Long courseId);
}
