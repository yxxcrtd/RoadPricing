package com.igoosd.rpss.pay.union.util;

import com.igoosd.common.util.HashKit;

import java.util.Map;
import java.util.TreeMap;

/**
 * 2018/3/21.
 */
public class SignUtil {

    /**
     * 签名
     * @param params
     * @param appkey
     * @return
     * @throws Exception
     */
    public static String sign(TreeMap<String,String> params, String appkey){
        if(params.containsKey("sign"))//签名明文组装不包含sign字段
            params.remove("sign");
        params.put("key", appkey);
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry:params.entrySet()){
            if(entry.getValue()!=null&&entry.getValue().toString().length()>0){
                sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append("&");
            }
        }
        if(sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
        String sign = HashKit.md5(sb.toString());//记得是md5编码的加签
        params.remove("key");
        return sign.toUpperCase();//大写返回
    }

    /***
     *验签
     * @param param
     * @param appkey
     * @return
     * @throws Exception
     */
    public static boolean validSign(TreeMap<String,String> param,String appkey){
        if(param!=null&&!param.isEmpty()){
            if(!param.containsKey("sign"))
                return false;
            String sign = param.get("sign").toString();
            String mySign = sign(param, appkey);
            return sign.equals(mySign);
        }
        return false;
    }
}
