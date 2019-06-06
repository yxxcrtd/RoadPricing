package com.igoosd.rpss.service;

import com.igoosd.domain.Member;

/**
 * 2018/2/7.
 */
public interface MemberService {

    /**
     * 获取用户会员身份
     * @param carNumber
     * @return
     */
    Member getCarNumberMemberType(String carNumber,Long subParkingId);
}
