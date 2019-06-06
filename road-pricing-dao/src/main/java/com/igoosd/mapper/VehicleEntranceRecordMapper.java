package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.model.TVehicleEntranceRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车辆进出场记录 Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface VehicleEntranceRecordMapper extends BaseMapper<TVehicleEntranceRecord> {


    int confirmVerExit(VehicleEntranceRecord record);


    /**
     * 获取指定条件下入场记录(未出场)
     * car_number, biz_status 为未完成状态 0,1
     *
     * @return
     */
    VehicleEntranceRecord getEntranceWithoutExitRecord(@Param("carNumber") String carNumber);


    /**
     * 根据车牌号查询当日停车未出场记录
     * @param carNUmber
     * @return
     */
    VehicleEntranceRecord getCurDateParkingVerInfo(@Param("carNumber") String carNUmber);

    /**
     * 查询指定时间范围内应收缴费总额（出场成功）
     * @return
     */
    BigDecimal getSumParkingChargeAmount(@Param("startDate")Date startDate,@Param("endDate") Date endDate);

    /**
     * 查询指定时间范围内 实际缴纳费用总额（出场成功）
     * @param startDate
     * @param endDate
     * @return
     */
    BigDecimal getSumRealParkingChargeAmount(@Param("startDate")Date startDate,@Param("endDate") Date endDate);


    /**
     * 分页查询
     * @param rb
     * @param vehicleEntranceRecord
     * @return
     */
    List<VehicleEntranceRecord> fuzzyFindPage(RowBounds rb,VehicleEntranceRecord vehicleEntranceRecord);


    /**
     * 查看停车记录详情
     * @param id
     * @return
     */
    VehicleEntranceRecord getDetailVerByKey(@Param("id") Long id);
}
