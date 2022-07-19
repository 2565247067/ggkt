package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.vod.mapper.CourseDescriptionMapper;
import com.atguigu.ggkt.vod.service.CourseDescriptionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
@Service
public class CourseDescriptionServiceImpl extends ServiceImpl<CourseDescriptionMapper, CourseDescription> implements CourseDescriptionService {

    @Override
    public void removeByCourseId(Long id) {
        baseMapper.delete(new QueryWrapper<CourseDescription>().eq("course_id",id));
    }
}