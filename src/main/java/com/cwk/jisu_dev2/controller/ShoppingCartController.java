package com.cwk.jisu_dev2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cwk.jisu_dev2.common.BaseContext;
import com.cwk.jisu_dev2.common.R;
import com.cwk.jisu_dev2.entity.ShoppingCart;
import com.cwk.jisu_dev2.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zzb04
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车商品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("已添加商品进购物车");
        log.info("获取的购物车数据参数：{}",shoppingCart);
        //设置用户id
        Long uid =  BaseContext.getTreadId();
        shoppingCart.setUserId(uid);
        //可能多次添加同样的商品，不需要重新添加，只改变数量即可
        //查询是否在购物车中是否已仅存在
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,uid);
        if (dishId!=null){
            //添加的是菜品
            // 可能存在口味不同的数据

            //注意，前端的菜品口味由于操作原因，口味只能选择一次，即第一次。可后期优化，在购物车中选择口味

            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            //queryWrapper.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
            //查询，判断是否口味相同
            ShoppingCart shoppingCartList = shoppingCartService.getOne(queryWrapper);
            if (shoppingCartList!=null)
            {
                //存在相同口味的饭菜
                Integer number = shoppingCartList.getNumber();
                shoppingCartList.setNumber(number + 1);
                shoppingCartService.updateById(shoppingCartList);
            }else{
                //为，新口味饭菜
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartService.save(shoppingCart);
                shoppingCartList = shoppingCart;

            }
            return R.success(shoppingCartList);
        }else{
            //添加的套餐
            // queryWrapper.eq(ShoppingCart::getSetmealId,dishId);
            ShoppingCart shoppingCartList1 =  shoppingCartService.getOne(queryWrapper);
            //查询当前菜品或者套餐是否在购物车中

            if(shoppingCartList1 != null){
                //如果已经存在，就在原来数量基础上加一
                Integer number = shoppingCartList1.getNumber();
                shoppingCartList1.setNumber(number + 1);
                shoppingCartService.updateById(shoppingCartList1);
            }else{
                //如果不存在，则添加到购物车，数量默认就是一
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartService.save(shoppingCart);
                shoppingCartList1 = shoppingCart;
            }
            return R.success(shoppingCartList1);
        }

    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("浏览购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getTreadId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //delete from shoppingCart where userId = ?
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getTreadId());
        shoppingCartService.remove(queryWrapper);
        return R.success("购物车已清空");
    }
}
