package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.User;
import com.igoosd.mapper.UserMapper;
import com.igoosd.model.TUser;
import com.igoosd.rps.service.UserService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/4/8.
 */
@Service
public class UserServiceImpl extends AbsCommonService<TUser,Long> implements UserService {

    @Autowired
    private UserMapper userMapper;



    @Override
    protected BaseMapper<TUser> getMapper() {
        return userMapper;
    }


    @Override
    public Page<User> findPageByFuzzy(User user, Page<User> page) {
        List<User> list = userMapper.findListByFuzzy(page,user);
        return page.setRecords(list);
    }
}
