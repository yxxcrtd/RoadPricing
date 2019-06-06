package com.igoosd.rpss.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 2018/3/5.
 */
public class StringUtils extends org.springframework.util.StringUtils {


    public static final List<Long> commaDelimitedListToLongList(String str) {
        String[] strs = commaDelimitedListToStringArray(str);
        if (null != strs) {
            List<Long> list = new LinkedList<>();
            for (String s : strs) {
                list.add(Long.valueOf(s));
            }
            return list;
        }
        return null;
    }

    public static final String splitStr(Object... strs){
        StringBuilder sb = new StringBuilder();
        for (Object str : strs){
            sb.append(str.toString());
        }
        return sb.toString();
    }
}
