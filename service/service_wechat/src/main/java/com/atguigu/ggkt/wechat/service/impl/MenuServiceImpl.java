package com.atguigu.ggkt.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.mapper.MenuMapper;
import com.atguigu.ggkt.wechat.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-19
 */
@Service
@AllArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private WxMpService wxMpService;

    @Override
    public List<MenuVo> findMenuInfo() {
        //1 创建list集合 用于数据的最终封装
        List<MenuVo> finalList = new ArrayList<>();
        //2 从所有菜单中数据里面，获得所有一级菜单
        List<Menu> oneList = findMenuOneInfo();
        //2 查询所有二级，菜单数据
        List<Menu> twoList = baseMapper.selectList(new QueryWrapper<Menu>().ne("parent_id", 0));

        //4 封装一级菜单数据
        for (Menu m:oneList) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(m,menuVo);
            //5 封装二级菜单 （判断一级菜单的id 是否跟二级菜单的parent_id是否一致
            List<MenuVo> tempList = new ArrayList<>();
            for (int i = 0; i < twoList.size(); i++) {
                if(twoList.get(i).getParentId().equals(menuVo.getId())){
                    MenuVo twoMenu = new MenuVo();
                    //类型转换
                    BeanUtils.copyProperties(twoList.get(i),twoMenu);
                    tempList.add(twoMenu);
                }
            }
            menuVo.setChildren(tempList);
            finalList.add(menuVo);
        }
        return  finalList;
    }

    @Override
    public List<Menu> findMenuOneInfo() {
        List<Menu> menus = baseMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", 0));
        return menus;
    }


    /**
     * 说明：
     * 自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
     * 一级菜单最多4个汉字，二级菜单最多8个汉字，多出来的部分将会以“...”代替。
     * 创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
     * 如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，
     * 如果菜单有更新，就会刷新客户端的菜单。测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
     */
    @Override
    public void syncMenu() {
        //借助json工具实现，把数据变成微信官方要求格式
        //获取所有菜单数据
        List<MenuVo> menuVoList = findMenuInfo();
        //封装button里面结构，数组格式
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo:menuVoList) {
            //json对象 一级菜单
            JSONObject one = new JSONObject();
            one.put("name",oneMenuVo.getName());
            //封装二级菜单
            JSONArray subButton = new JSONArray();
            for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if(twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
                    view.put("url", "http://lhweb1.vaiwan.com/#"
                            +twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button",subButton);
            buttonList.add(one);
        }
        //封装最外层菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            this.wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
