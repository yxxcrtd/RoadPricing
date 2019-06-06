package com.igoosd.rpss.service;

import com.igoosd.rpss.vo.PrintInfo;

import java.util.List;

/**
 * 2018/4/18.
 *  打印服务 获取打印服务信息
 *  此接口仅限pos终端使用
 */

public interface PrintService {

    List<PrintInfo> getEnterReceipt(Long verId);

    List<PrintInfo> getExitReceipt(Long verId);

    List<PrintInfo> printByOrderId(Long orderId);
}
