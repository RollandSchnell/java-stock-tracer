package com.stock.app.util;

import java.sql.Timestamp;

/**
 * Util class for timestamp values.
 *
 * @author rolland.schnell
 */
public class DateUtils {

    /**
     * Return the current time used for timestamp logging.
     * @return current time as String
     */
    static public String getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }
}
