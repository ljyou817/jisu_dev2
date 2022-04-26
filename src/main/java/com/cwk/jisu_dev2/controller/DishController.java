package com.cwk.jisu_dev2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwk.jisu_dev2.common.R;
import com.cwk.jisu_dev2.dto.DishDto;
import com.cwk.jisu_dev2.entity.Category;
import com.cwk.jisu_dev2.entity.Dish;
import com.cwk.jisu_dev2.entity.DishFlavor;
import com.cwk.jisu_dev2.service.CategoryService;
import com.cwk.jisu_dev2.service.DishFlavorService;
import com.cwk.jisu_dev2.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzb04
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 封装前端传递的数据
     *
     * 新增菜品
     *
     * 导入DTO，传输数据与实体类不是一一对应时使用
     *  用于展示层与服务层之间
     *
     *  由于前端提交的是json数据，所以要使用RequestBody
     *
     * @return
     */
    @PostMapping
    private R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        //由于要同时操作两张表，所以需要自定义Service层
        dishService.saveDishWithFlavor(dishDto);
        return R.success("菜品，新增成功");
    }

    /**
     * 菜品的分页查询功能
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //解决分类名称1
        Page<DishDto> dishDtoPage = new Page<>();
        //条件过滤器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤的使用条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        //返回的数据，只有菜品分类id，但是没有菜品分类名称，故分类名称无法展示

        //解决分类名称2
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();

        //这里没有使用多表连接进行查询，为的是提高数据库查表的速度，单表查询比连表查询快很多
        //即使现在这种处理方式会增加系统的开销，在内存上多次的复制，和类型转化操作
        List<DishDto> list =  records.stream().map((item)->{
            //新建dishDto对象，封装所有的对象数据
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //获取分类id，并通过分类id，得到分类名称，并将其赋值dishDto
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        //通过dishDtoPage封装好的dishDto
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        log.info("开始修改，菜品信息");
        DishDto dishDto =  dishService.getDishWithFlavorById(id);
        return R.success(dishDto);

    }

    /**
     * 封装前端传递的数据
     *
     * 修改菜品
     *
     * 导入DTO，传输数据与实体类不是一一对应时使用
     *  用于展示层与服务层之间
     *
     *  由于前端提交的是json数据，所以要使用RequestBody
     *
     * @return
     */
    @PutMapping
    private R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        //由于要同时操作两张表，所以需要自定义Service层
        dishService.updateDishWithFlavor(dishDto);
        return R.success("菜品，修改成功");
    }

    /**
     *
     * 根据条件查询分类信息，为添加页面展示使用
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //条件筛选
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId,dish.getCategoryId());
        //查询状态为1的菜品，即在售的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        //这里没有使用多表连接进行查询，为的是提高数据库查表的速度，单表查询比连表查询快很多
        //即使现在这种处理方式会增加系统的开销，在内存上多次的复制，和类型转化操作
        List<DishDto> dishDtoList =  list.stream().map((item)->{
            //新建dishDto对象，封装所有的对象数据
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //获取分类id，并通过分类id，得到分类名称，并将其赋值dishDto
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                dishDto.setCategoryName(category.getName());
            }
            //查询口味信息
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dishId);
            //SQL：select * from dish_flavors where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }

    /**
     * 菜品的删除功能
     * 同时实现对批量删除的操作
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("要删除的ids:{}",ids);
        dishService.removeDish(ids);
        return R.success("菜品，删除成功");
    }


    /**
     *
     * 菜品的停售或起售
     * 同时实现对批量停售或起售的操作
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable int status,@RequestParam List<Long> ids){
        log.info("启停售状态：{}。操作的菜品id：{}",status,ids);
        dishService.updateDish(status,ids);
        return R.success("修改成功");
    }
}
