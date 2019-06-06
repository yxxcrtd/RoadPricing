package com.igoosd.rpss.service.impl;

import com.igoosd.domain.Member;
import com.igoosd.mapper.MemberMapper;
import com.igoosd.rpss.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/2/7.
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member getCarNumberMemberType(String carNumber,Long subParkingId) {
        return memberMapper.getMemberInfoByCarNumber(carNumber,subParkingId);
    }
}
