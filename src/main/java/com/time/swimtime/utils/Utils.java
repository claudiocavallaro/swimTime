package com.time.swimtime.utils;

public class Utils {

    public static PropertyService getProperty() {
        return ContextAccessor.getBean(PropertyService.class);
    }
}