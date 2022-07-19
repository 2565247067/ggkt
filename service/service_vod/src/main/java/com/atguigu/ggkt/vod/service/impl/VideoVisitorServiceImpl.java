package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.VideoVisitor;

import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.atguigu.ggkt.vo.vod.VideoVisitorVo;
import com.atguigu.ggkt.vod.mapper.VideoVisitorMapper;
import com.atguigu.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {


    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        //调用mapper方法
        List<VideoVisitorCountVo> videoVisitorVoList=baseMapper.findCount(courseId,startDate,endDate);
        //创建map
        HashMap<String, Object> map = new HashMap<>();
        //创建两个list 一个代表日期（横坐标） 一个代表数量（纵坐标）
        List<String> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();

        for (VideoVisitorCountVo v:videoVisitorVoList) {
            dateList.add(v.getJoinTime());
            countList.add(v.getUserCount());
        }
        //封装map
        map.put("xData",dateList);
        map.put("yData",countList);
        return map;
    }
}
