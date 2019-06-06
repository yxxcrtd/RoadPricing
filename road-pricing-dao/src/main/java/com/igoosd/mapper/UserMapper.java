package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.User;
import com.igoosd.model.TUser;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface UserMapper extends BaseMapper<TUser> {


    List<User> findListByFuzzy(Page page,User user);
}
