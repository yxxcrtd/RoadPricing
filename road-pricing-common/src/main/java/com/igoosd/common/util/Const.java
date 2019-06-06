package com.igoosd.common.util;

/**
 * 2018/2/5.
 */
public interface Const {

    /**
     * RSA加密 明文分隔符
     */
    String PLAIN_TEXT_SPLIT = "&";

    String PUBLIC_KEY = "publicKey";
    String PRIVATE_KEY = "privateKey";
    String KEY_INDEX = "keyIndex";

    /**
     * DES加密 key
     */
    String DES_SERCET = "igoosd-server^&%$##";

    /**
     * token 存储到redis  key 为  {REDIS_TOKEN_PRE_KEY} + {collectorId}
     */
    String REDIS_TOKEN_PRE_KEY = "token:";

    /**
     * 登录用户关联当前的sessionId  用于同一个账号只允许登录一台设备
     * SESSION_USER:{collectorId}
     */
    String REDIS_SESSION_USER_PRE_KEY = "SESSION_USER:";

    /**
     * redis hash 存储 入场车辆信息 field:车位ID  value:  车位信息 json字符串
     */
    String REDIS_PARKING_SPACE_HASH_KEY = "parking_space";

    /**
     * redis  k-v 存储 预出场 车辆信息
     */
    String REDIS_VER_EXIT_CAR_INFO_PREKEY = "ver_exit_car_info:";


    String  REDIS_CHARGE_RULE_HASH_KEY="charge_rule";

    /**
     * 系统配置项key
     */
    String REDIS_SYS_CONFIG_KEY = "sys_config";


    /**
     * 登录后存储的登录用户相关信息
     */
    String SESSION_USER = "login_user";

    String SESSION_TOKEN = "token";

    /**
     * 登录后 客户端访问资源服务 请求头需带的参数
     */
    String HEADER_SESSION_ID = "Session-Id";
    String HEADER_SIGNATURE = "Signature";


    int DAY_MINUTES = 24 * 60;



}
