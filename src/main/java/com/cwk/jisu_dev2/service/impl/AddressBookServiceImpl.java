package com.cwk.jisu_dev2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwk.jisu_dev2.entity.AddressBook;
import com.cwk.jisu_dev2.mapper.AddressBookMapper;
import com.cwk.jisu_dev2.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author zzb04
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
