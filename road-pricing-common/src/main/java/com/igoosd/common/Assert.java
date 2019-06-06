package com.igoosd.common;

import com.igoosd.common.exception.RoadPricingException;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * 2017/9/18.
 */
public class Assert {

    private static MessageFormatter messageFormatter = new MessageFormatter();

    public static void notNull(Object obj, String message, Object... args) {
        if (null == obj) {
            throw new RoadPricingException(formatterInfo(message, args));
        }
    }

    public static void hasText(String str, String message, Object... args) {
        if (null == str || str.trim().equals("")) {
            throw new RoadPricingException(formatterInfo(message, args));
        }
    }

    public static void isTrue(boolean flag, String message, Object... args) {
        if (!flag) {
            throw new RoadPricingException(formatterInfo(message, args));
        }
    }

    public static String formatterInfo(String message, Object... args) {
        FormattingTuple formattingTuple = messageFormatter.arrayFormat(message, args);
        return formattingTuple.getMessage();
    }

}
