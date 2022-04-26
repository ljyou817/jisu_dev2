package com.cwk.jisu_dev2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwk.jisu_dev2.common.R;
import com.cwk.jisu_dev2.entity.Category;
import com.cwk.jisu_dev2.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理的控制层
 *
 * @author zzb04
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("分类新增成功!");
    }

    /**
     *
     * 分页查询，分类页面展示
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page={},pageSize={}",page,pageSize);
        //构造分页构造器功能
        Page<Category> page1 = new Page<>(page,pageSize);
        //定义查询条件,过滤条件.条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //查询
        categoryService.page(page1,queryWrapper);
        return R.success(page1);
    }

    /**
     * 根据分类id进行删除
     * 由于是对分类进行删除，数据价值低并不具备可重用性，故直接进行真删除
     * 后期可优化
     * 同时判断是否有菜品和套餐管理按
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，分类id为{}",ids);
        //自定义删除方法
        categoryService.remove(ids);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id进行分类修改
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改后分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("分类信息修改成功");

    }

    /**
     *
     * 根据条件查询分类信息，为添加页面展示使用
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件筛选
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
