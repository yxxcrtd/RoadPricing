package com.igoosd.rps.util;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/8.
 */
@Data
@ToString
public class SessionUser {

    private Long id;

    private String loginName;

    private String userName;

    private String password;

    private Boolean sex;

    private String phone;

    private String ip;

    private String[] permissions;

    private Boolean remember = false;


}
