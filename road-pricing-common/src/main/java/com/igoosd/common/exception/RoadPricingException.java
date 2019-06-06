package com.igoosd.common.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * 2018/1/17.
 */
public class RoadPricingException extends RuntimeException {


    private static MessageFormatter messageFormatter = new MessageFormatter();

    public RoadPricingException(String message) {
        super(message);
    }

    public RoadPricingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RoadPricingException(String message, String... vars) {
        this(messageFormatter.arrayFormat(message, vars).getMessage());
    }

    public RoadPricingException(String message, Throwable throwable, String... vars) {
        this(messageFormatter.arrayFormat(message, vars).getMessage(), throwable);
    }
}
