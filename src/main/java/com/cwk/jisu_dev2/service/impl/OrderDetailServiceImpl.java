package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.entity.OrderDetail;
import com.cwk.jisu_dev2.mapper.OrderDetailMapper;
import com.cwk.jisu_dev2.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author zzb04
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
