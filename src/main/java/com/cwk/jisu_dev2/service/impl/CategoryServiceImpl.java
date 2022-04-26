package com.cwk.jisu_dev2.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.common.CustomException;
import com.cwk.jisu_dev2.entity.Category;
import com.cwk.jisu_dev2.entity.Dish;
import com.cwk.jisu_dev2.entity.Setmeal;
import com.cwk.jisu_dev2.mapper.CategoryMapper;
import com.cwk.jisu_dev2.service.CategoryService;
import com.cwk.jisu_dev2.service.DishService;
import com.cwk.jisu_dev2.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzb04
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id进行判断删除
     * 是否关联菜品和套餐
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //统计关联菜品
        int i = dishService.count(dishLambdaQueryWrapper);
        if (i>0){
            //已关联菜品，抛出异常，自定义业务异常
            throw new CustomException("当前分类，已关联菜品，无法删除");
        }
        //统计关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        //统计关联套餐
        int j = setmealService.count(setmealLambdaQueryWrapper);
        if (j>0){
            //已关联套餐，抛出异常
            throw new CustomException("当前分类，已关联套餐，无法删除");
        }
        //无关联，可直接删除
    }
}
