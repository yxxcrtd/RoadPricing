package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.ChargeRule;
import com.igoosd.model.TChargeRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface ChargeRuleMapper extends BaseMapper<TChargeRule> {

    /**
     *  根据停车场ID查询关联的收费规则
     * @param parkingId
     * @return
     */
    ChargeRule getChargeRuleByParkingId(@Param("parkingId") Long parkingId);

    /**
     * 获取所有停车场收费列表
     * @return
     */
    List<ChargeRule> getTotalParkingChargeRuleList();

}
