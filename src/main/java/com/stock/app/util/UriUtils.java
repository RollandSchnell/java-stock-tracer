package com.stock.app.util;

import java.util.Map;

public class UriUtils {

    static public String buildUrlWithQUeryParams(String url, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder(url);

        if (queryParams != null) {
            sb.append("?");
            queryParams.forEach( (k, v) -> sb.append("&").append(k).append("=").append(v));
        }

        return sb.toString();
    }
}
