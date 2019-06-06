package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.Member;
import com.igoosd.model.TMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface MemberMapper extends BaseMapper<TMember> {


    /**
     * 获取当日车辆会员信息
     *
     * @return
     */
    Member getMemberInfoByCarNumber(@Param("carNumber") String carNumber, @Param("subParkingId") Long subParkingId);

    /**
     * 获取驶入时间起有效的会员列表
     */
    List<Member> getMemberListByCarNumber(@Param("carNumber") String carNumber, @Param("subParkingId") Long subParkingId, @Param("enterTime") Date enterTime);

    List<Member> fuzzyFindPage(RowBounds rb, Member member);

    /**
     * 获取车牌最新会员记录
     * @param carNumber
     * @return
     */
    Member getLatestMember(@Param("carNumber") String carNumber);
}
