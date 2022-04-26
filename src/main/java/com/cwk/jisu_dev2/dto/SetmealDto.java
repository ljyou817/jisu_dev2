package com.cwk.jisu_dev2.dto;

import com.cwk.jisu_dev2.entity.Setmeal;
import com.cwk.jisu_dev2.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
