package com.igoosd.common.util;


import java.util.regex.Pattern;

/**
 * 2018/5/9.
 * 正则工具类
 */
public class RegexUtils {


    public static boolean isPhone(String str){
        if(null != str && !"".equals(str)){
            return Pattern.matches("^[1][0-9]{10}$",str);
        }
        return false;
    }

    public static boolean isTime(String str){
        if(null != str && !"".equals(str)){
            boolean flag =  Pattern.matches("^\\d{1,2}[:]\\d{1,2}$",str);
            if(flag){
                String[] ss = str.split(":");
                if(Integer.parseInt(ss[0])<24 && Integer.parseInt(ss[1]) <60){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCarNumber(String carNumber){
        String regex ="^([冀豫云辽黑湘皖鲁苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼渝京津沪新京军空海北沈兰济南广成使领][a-zA-Z](([DF]([a-zA-Z0-9]*[^iIoO])[0-9]{4})|([0-9]{5}[DF])))|([冀豫云辽黑湘皖鲁苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼渝京津沪新京军空海北沈兰济南广成使领A-Z][a-zA-Z0-9]{5}[a-zA-Z0-9挂学警港澳])$";
        return Pattern.matches(regex,carNumber);
    }

    public static boolean isGeoLocation(String geoLocation){
        return Pattern.matches("^\\d{1,3}[.]\\d{4,}+[,]\\d{1,3}[.]\\d{4,}$",geoLocation);
    }

    public static void main(String[] args) {
        System.out.println(isGeoLocation("99:00"));
        System.out.println(isGeoLocation("11,11"));

        System.out.println(isGeoLocation("11.111,11.1111"));
        System.out.println(isGeoLocation("11.1111,22.111111"));


    }

}


