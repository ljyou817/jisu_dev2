package com.cwk.jisu_dev2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwk.jisu_dev2.common.BaseContext;
import com.cwk.jisu_dev2.common.R;
import com.cwk.jisu_dev2.entity.Category;
import com.cwk.jisu_dev2.entity.Orders;
import com.cwk.jisu_dev2.entity.Setmeal;
import com.cwk.jisu_dev2.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zzb04
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    /**
     * 用户下单功能
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody  Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("订单下单成功");
    }
    /**
     * 根据条件进行查询订单
     * @param
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        log.info("page={},pageSize={}",page,pageSize);
        Long uid = BaseContext.getTreadId();
        //构造分页构造器功能
        Page<Orders> page1 = new Page<>(page,pageSize);
        //定义查询条件,过滤条件.条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,uid);
        //添加排序条件
        queryWrapper.orderByAsc(Orders::getOrderTime);
        //查询
        orderService.page(page1,queryWrapper);
        return R.success(page1);
    }

}
