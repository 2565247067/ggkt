package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Chapter;

import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vo.vod.VideoVo;
import com.atguigu.ggkt.vod.mapper.ChapterMapper;
import com.atguigu.ggkt.vod.service.ChapterService;
import com.atguigu.ggkt.vod.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    private VideoService videoService;

    @Override
    public List<ChapterVo> getTreeList(Long courseId) {
        //定义最终数据list集合
        List<ChapterVo> finalChapterList = new ArrayList<>();

        //根据课程id获取所有课程章节
        List<Chapter> chapterList = baseMapper.selectList(new QueryWrapper<Chapter>().eq("course_id", courseId));

        //根据课程id获取所有小节
        LambdaQueryWrapper<Video> videoWrapper = new LambdaQueryWrapper<>();
        videoWrapper.eq(Video::getCourseId,courseId);
        List<Video> videoList = videoService.list(videoWrapper);

        //封装章节
        for (int i = 0; i < chapterList.size(); i++) {
            //得到课程里面所有章节
            Chapter chapter = chapterList.get(i);
            //chaper->chaperVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);

            //封装章节里面的小节
            ArrayList<VideoVo> videoVoList = new ArrayList<>();
            for (Video video:videoList) {
                //判断小节是否属于章节
                if(chapter.getId().equals(video.getChapterId())){
                    //video->vidieVo
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    //放入videoVoList
                    videoVoList.add(videoVo);
                }
            }
            //把章节里面所有的小节集合放入章节里面
            chapterVo.setChildren(videoVoList);
            //封装到最终集合
            finalChapterList.add(chapterVo);
        }
        return finalChapterList;
    }

    @Override
    public void removeByCourseId(Long id) {
        baseMapper.delete(new QueryWrapper<Chapter>().eq("course_id",id));
    }
}
