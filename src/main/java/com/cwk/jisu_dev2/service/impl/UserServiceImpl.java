package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.entity.User;
import com.cwk.jisu_dev2.mapper.UserMapper;
import com.cwk.jisu_dev2.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author zzb04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
