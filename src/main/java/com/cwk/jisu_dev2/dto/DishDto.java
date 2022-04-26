package com.cwk.jisu_dev2.dto;


import com.cwk.jisu_dev2.entity.Dish;
import com.cwk.jisu_dev2.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据传输对象
 * @author zzb04
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
