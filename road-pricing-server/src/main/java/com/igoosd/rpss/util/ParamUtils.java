package com.igoosd.rpss.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2018/2/6.
 */
public class ParamUtils {


    /**
     * 解析http请求 Params 并排序
     *
     * @param request
     * @return
     */
    public static Map<String, String> parseRequestParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> resultMap;
        if (null == parameterMap || parameterMap.size() == 0) {
            return new HashMap<>(0);
        }
        resultMap = new HashMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().length > 0) {
                resultMap.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        return resultMap;
    }

    /**
     *  request 请求 参数 排序
     * @param request
     * @return
     */
    public static String sortRequestFormParams(HttpServletRequest request) {
        TreeMap<String, String> treeMap = new TreeMap<>(parseRequestParams(request));
        StringBuilder sb = new StringBuilder("");
        if (treeMap.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = treeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            //去除最后一个 ‘&’字符
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
        }
        return sb.toString();
    }


}
