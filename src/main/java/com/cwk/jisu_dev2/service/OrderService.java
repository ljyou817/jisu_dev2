package com.cwk.jisu_dev2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cwk.jisu_dev2.entity.Orders;

/**
 * @author zzb04
 */
public interface OrderService extends IService<Orders> {
    //用户下单
    public void submit(Orders orders);
}
