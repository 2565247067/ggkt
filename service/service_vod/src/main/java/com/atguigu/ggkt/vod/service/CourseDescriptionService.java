package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.CourseDescription;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
public interface CourseDescriptionService extends IService<CourseDescription> {

    void removeByCourseId(Long id);
}
