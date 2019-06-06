package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.mapper.MemberTypeMapper;
import com.igoosd.model.TMemberType;
import com.igoosd.rps.service.MemberTypeService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/5/9.
 */
@Service
public class MemberTypeServiceImpl extends AbsCommonService<TMemberType,Long> implements MemberTypeService {

    @Autowired
    private MemberTypeMapper memberTypeMapper;


    @Override
    protected BaseMapper<TMemberType> getMapper() {
        return memberTypeMapper;
    }
}
