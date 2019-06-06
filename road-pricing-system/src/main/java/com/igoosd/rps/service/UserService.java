package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.User;
import com.igoosd.model.TUser;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/4/8.
 */
public interface UserService extends CommonService<TUser,Long> {


    /**
     * 分页查询用户
     * @param user
     * @param page
     * @return
     */
    Page<User> findPageByFuzzy(User user, Page<User> page);
}
