package com.cwk.jisu_dev2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwk.jisu_dev2.common.R;
import com.cwk.jisu_dev2.dto.SetmealDto;
import com.cwk.jisu_dev2.entity.Category;
import com.cwk.jisu_dev2.entity.Setmeal;
import com.cwk.jisu_dev2.service.CategoryService;
import com.cwk.jisu_dev2.service.SetmealDishService;
import com.cwk.jisu_dev2.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 *
 * @author zzb04
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("开始新增套餐，套餐信息：{}",setmealDto);
        setmealService.saveSetmealWithDish(setmealDto);
        return R.success("套餐，新增成功");
    }

    /**
     * 套餐的分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Setmeal> page1 = new Page<>(page,pageSize);
        //解决套餐分类1
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //添加查询和排序条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(page1,queryWrapper);


        //解决套餐分类2
        BeanUtils.copyProperties(page1,setmealDtoPage,"records");
        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //通过分类id得到分类name
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;

        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 套餐的删除功能
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("要删除的ids:{}",ids);
        setmealService.removeSetmealWithDish(ids);
        return R.success("套餐，删除成功");
    }

    /**
     *
     * 套餐的停售或起售
     * 同时实现对批量停售或起售的操作
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable int status,@RequestParam List<Long> ids){
        log.info("启停售状态：{}。操作的套餐id：{}",status,ids);
        setmealService.updateSetmeal(status, ids);
        return R.success("修改成功");
    }

    /**
     * 根据条件进行查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

}
