package com.atguigu.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.atguigu.ggkt.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 25652
 * @time: 2022/7/16 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @ExcelProperty(value = "用户编号",index = 0)
    private int id;

    @ExcelProperty(value = "用户名称",index = 1)
    private String name;

}
