package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Video;

import com.atguigu.ggkt.vod.mapper.VideoMapper;
import com.atguigu.ggkt.vod.service.VideoService;
import com.atguigu.ggkt.vod.service.VodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-17
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {


    @Autowired
    private VodService vodService;

    //根据课程id删除小节，并删除小节内所有视频
    @Override
    public void removeVideoByCourseId(Long courseId) {
        //根据课程id 查询所有课程小节
        List<Video> videoList = baseMapper.selectList(new QueryWrapper<Video>().eq("course_id", courseId));
        //遍历所有小节集合得到每个小节视频id
        for (Video v:videoList) {
            //若视频id不为空 则删除视频
            String sourceId = v.getVideoSourceId();
            if(!StringUtils.isEmpty(sourceId)){
                vodService.removeVideo(sourceId);
            }
        }

        //删除所有小节
        baseMapper.delete(new QueryWrapper<Video>().eq("course_id",courseId));
    }



    //删除小节的时候删除视频
    @Override
    public void removeVideoByVideoId(Long id) {
        //id 查询小节信息
        Video video = baseMapper.selectById(id);
        String sourceId = video.getVideoSourceId();

        //视频id不为空则删除视频
        if(!StringUtils.isEmpty(sourceId)){
            vodService.removeVideo(sourceId);
        }
        baseMapper.deleteById(id);

    }

    //根据章节id删除小节，并删除小节内所有视频
    @Override
    public void removeVideoByChapterId(Long id) {

        //根据课程id 查询所有课程小节
        List<Video> videoList = baseMapper.selectList(new QueryWrapper<Video>().eq("chapter_id",id));
        //遍历所有小节集合得到每个小节视频id
        for (Video v:videoList) {
            //若视频id不为空 则删除视频
            String sourceId = v.getVideoSourceId();
            if(!StringUtils.isEmpty(sourceId)){
                vodService.removeVideo(sourceId);
            }
        }
        //删除所有小节
        baseMapper.delete(new QueryWrapper<Video>().eq("chapter_id",id));
    }
}
