package com.stock.app.util;

import java.sql.Timestamp;

public class DateUtils {

    static public String getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }
}
