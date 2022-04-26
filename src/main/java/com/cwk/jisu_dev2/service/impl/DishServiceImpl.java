package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.common.CustomException;
import com.cwk.jisu_dev2.dto.DishDto;
import com.cwk.jisu_dev2.entity.Dish;
import com.cwk.jisu_dev2.entity.DishFlavor;
import com.cwk.jisu_dev2.mapper.DishMapper;
import com.cwk.jisu_dev2.service.DishFlavorService;
import com.cwk.jisu_dev2.service.DishService;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzb04
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     *     涉及多表操作，需开启注释
     */
    @Transactional
    @Override
    public void saveDishWithFlavor(DishDto dishDto) {
        //该步骤只保留dish表
        this.save(dishDto);
        Long dishId = dishDto.getId();
        //进一步insert,dish_flavor表
        //注意，这步，无法将菜品id封装进dish_flavor
        //dishFlavorService.saveBatch(dishDto.getFlavors());
        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorList);
    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlavorById(Long id) {
        //查询菜品基本信息，dish
        Dish dish = this.getById(id);
        //查询菜品口味,dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());

        //得到相对应的菜品口味
        List<DishFlavor> flavorList = dishFlavorService.list(queryWrapper);
        //定义DishDto,用于返回值
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(flavorList);

        return dishDto;
    }

    /**
     * 更细菜品和菜品口味表
     * @param dishDto
     */
    @Override
    public void updateDishWithFlavor(DishDto dishDto) {
        //更新菜品信息表
        this.updateById(dishDto);
        //更新菜品口味表
        //该步骤由于口味信息过多和数据类型较复杂，所以可以先删除，在添加
        //故口味表删除
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //口味表添加
        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorList);

    }

    /**
     * 菜品的删除功能
     *
     * @param ids
     */
    @Override
    public void removeDish(List<Long> ids) {
        //查询菜品的状态，是否为停售
        //select count(*) from dish where id in (1,2,3) and status = 1;
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.in(Dish::getId,ids);
        int count = this.count(queryWrapper);
        if (count>0){
            throw new CustomException("菜品正在售卖，无法删除");
        }
        //可以删除
        this.removeByIds(ids);
    }

    /**
     * 菜品停或起售功能的实现
     * @param status
     * @param ids
     */
    @Override
    public void updateDish(int status, List<Long> ids) {
        //查询菜品的状态，如果菜品正在售卖则无法起售
        //status字段表示为改变后的状态
        //selectcount(*) from dish where id in(1,2,3) and status = 1;
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,status);
        int count = this.count(queryWrapper);
        //如果出现改变后状态与现在状态相同则无法改变状态
        if (count>0){
            throw new CustomException("菜品状态已达预期，无需改变");
        }
        //可以改变状态
        LambdaUpdateWrapper<Dish> queryWrapper1 = new LambdaUpdateWrapper<>();
        queryWrapper1.in(Dish::getId,ids).set(Dish::getStatus,status);
        this.update(queryWrapper1);
    }
}
