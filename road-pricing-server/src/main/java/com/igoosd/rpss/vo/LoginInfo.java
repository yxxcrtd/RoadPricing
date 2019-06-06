package com.igoosd.rpss.vo;

import lombok.Data;

/**
 * 2018/2/5.
 */
@Data
public class LoginInfo {

    private  String cipherText;
    private String publicKey;
    private String location;//地理位置

}
