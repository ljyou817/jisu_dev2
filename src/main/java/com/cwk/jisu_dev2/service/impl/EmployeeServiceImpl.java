package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.entity.Employee;
import com.cwk.jisu_dev2.service.EmployeeService;
import com.cwk.jisu_dev2.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
 * @author zzb04
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
