package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.SubParking;
import com.igoosd.model.TSubParking;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/9.
 */
public interface SubParkingService extends CommonService<TSubParking,Long> {


    Page<SubParking> fuzzyFindPage(SubParking subParking,Page page);
}
