package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.common.CustomException;
import com.cwk.jisu_dev2.dto.SetmealDto;
import com.cwk.jisu_dev2.entity.Dish;
import com.cwk.jisu_dev2.entity.Setmeal;
import com.cwk.jisu_dev2.entity.SetmealDish;
import com.cwk.jisu_dev2.mapper.SetmealMapper;
import com.cwk.jisu_dev2.service.SetmealDishService;
import com.cwk.jisu_dev2.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzb04
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐是同时修改套餐菜品关联表
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveSetmealWithDish(SetmealDto setmealDto) {
        //保持套餐表
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的相关行表
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeSetmealWithDish(List<Long> ids) {
        //查询套餐状态，是否为停售，判断是否可以删除
        //select count(*) from setmeal where id in(1,2,3) and status = 1;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if ( count > 0){
            throw new CustomException("套餐正在售卖中，无法进行删除");
        }
        //如果可以删除，即删除
        this.removeByIds(ids);
        //删除关系表
        //delete from setmeal_dish where setmeal_id in(1,2,3);
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(queryWrapper1);
    }


    /**
     * 套餐的停或起售功能
     * @param status
     * @param ids
     */
    @Override
    public void updateSetmeal(int status, List<Long> ids) {
        //查询套餐的状态，如果套餐正在售卖则无法起售
        //status字段表示为改变后的状态
        //select count(*) from setmeal where id in(1,2,3) and status = 1;
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,status);
        int count = this.count(queryWrapper);
        //如果出现改变后状态与现在状态相同则无法改变状态
        if (count>0){
            throw new CustomException("套餐状态已达预期，无需改变");
        }
        //可以改变状态
        LambdaUpdateWrapper<Setmeal> queryWrapper1 = new LambdaUpdateWrapper<>();
        queryWrapper1.in(Setmeal::getId,ids).set(Setmeal::getStatus,status);
        this.update(queryWrapper1);
    }
}
