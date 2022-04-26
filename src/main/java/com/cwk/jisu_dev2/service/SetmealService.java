package com.cwk.jisu_dev2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cwk.jisu_dev2.dto.SetmealDto;
import com.cwk.jisu_dev2.entity.Setmeal;

import java.util.List;

/**
 * @author zzb04
 */
public interface SetmealService extends IService<Setmeal> {

    public void saveSetmealWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeSetmealWithDish(List<Long> ids);

    //菜品的停售或起售
    public void updateSetmeal(int status,List<Long> ids);
}
