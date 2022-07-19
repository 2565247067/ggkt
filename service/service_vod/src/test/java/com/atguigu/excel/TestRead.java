package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;

/**
 * @description:
 * @author: 25652
 * @time: 2022/7/16 17:27
 */
public class TestRead {

    public static void main(String[] args) {
        //设置文件名和路径
        String fileName="D:\\aiguigu.xlsx";

        //调用方法进行读操作
        EasyExcel.read(fileName,User.class,new ExcelListener()).sheet().doRead();
    }
}
