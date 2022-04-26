package com.cwk.jisu_dev2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cwk.jisu_dev2.dto.DishDto;
import com.cwk.jisu_dev2.entity.Dish;

import java.util.List;

/**
 * @author zzb04
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时insert两张表，dish,dish_flavor
    public void saveDishWithFlavor(DishDto dishDto);

    //修改菜品时，查询菜品的相关信息
    public DishDto getDishWithFlavorById(Long id);

    //修改菜品的更改信息
    public void updateDishWithFlavor(DishDto dishDto);

    //菜品的删除功能实现
    public void removeDish(List<Long> ids);

    //菜品的停售或起售
    public void updateDish(int status,List<Long> ids);
}
