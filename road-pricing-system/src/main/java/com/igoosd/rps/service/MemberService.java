package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Member;
import com.igoosd.model.TMember;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/10.
 */
public interface MemberService extends CommonService<TMember,Long> {


    /**
     * 分页模糊查询会员列表
     * @param member
     * @param page
     * @return
     */
    Page<Member> fuzzyFindPage(Member member,Page<Member> page);

    /**
     * 获取指定车牌最新会员记录
     * @param carNumber
     * @return
     */
    Member getLatestMember(String carNumber);
}
