package com.booking.utils;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@SuppressWarnings("serial")
public class AbstractValueObject implements IValueObject {
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
