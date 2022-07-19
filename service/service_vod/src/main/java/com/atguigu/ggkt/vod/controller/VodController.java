package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.VodService;
import com.atguigu.ggkt.vod.utils.ConstantPropertiesUtil;
import com.atguigu.ggkt.vod.utils.Signature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * @description:
 * @author: 25652
 * @time: 2022/7/19 9:30
 */
@Api(tags = "腾讯云点播")
@RestController
@RequestMapping("/admin/vod")

@AllArgsConstructor
public class VodController {
    private VodService vodService;

    @ApiOperation("返回客户端签名")
    @GetMapping("sign")
    public Result sign() {
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return Result.Ok(signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
            return Result.Error("获取签名失败");
        }
    }

    @ApiOperation("上传视频接口")
    @PostMapping("upload")
    public Result upload(){
        String filedId = vodService.uploadVideo();
        return  Result.Ok(filedId);
    }

    @ApiOperation("删除腾讯云中的视频")
    @DeleteMapping("remove/{fileId}")
    public Result remove(@PathVariable("fileId") String fileId){
        vodService.removeVideo(fileId);
        return Result.Ok();
    }


}