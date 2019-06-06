package com.igoosd.rpss.service;

import com.igoosd.model.TCollector;

/**
 * 2018/2/5.
 * 收费员 服务
 */
public interface CollectorService {


    /**
     * 收费员登录校验
     * @param jobNumber
     * @param password
     * @return 校验成功返回 持久层对象 失败 返回null
     */
    TCollector loginVerify(String jobNumber, String password);


    /**
     * 收费员修改密码
     * @param oldPwd
     * @param newPwd
     */
    void modifyPwd(String oldPwd,String newPwd);


    TCollector getCollectorById(Long id);
}
