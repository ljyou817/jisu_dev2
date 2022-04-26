package com.cwk.jisu_dev2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cwk.jisu_dev2.entity.Category;

/**
 * @author zzb04
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
