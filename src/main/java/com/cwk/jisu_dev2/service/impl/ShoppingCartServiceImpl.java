package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.entity.ShoppingCart;
import com.cwk.jisu_dev2.mapper.ShoppingCartMapper;
import com.cwk.jisu_dev2.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author zzb04
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
