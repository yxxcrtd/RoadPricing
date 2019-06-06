package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Member;
import com.igoosd.mapper.MemberMapper;
import com.igoosd.model.TMember;
import com.igoosd.rps.service.MemberService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/10.
 */
@Service
public class MemberServiceImpl extends AbsCommonService<TMember, Long> implements MemberService {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    protected BaseMapper<TMember> getMapper() {
        return memberMapper;
    }

    @Override
    public Page<Member> fuzzyFindPage(Member member, Page<Member> page) {
        List<Member> list = memberMapper.fuzzyFindPage(page, member);
        return page.setRecords(list);
    }

    @Override
    public Member getLatestMember(String carNumber) {
        return memberMapper.getLatestMember(carNumber);
    }
}
