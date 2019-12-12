package com.stock.app.util;

import java.util.Map;

/**
 * Util class for URI resource building and parsing.
 *
 * @author rolland.schnell
 */
public class UriUtils {

    /**
     * Build a URL with query params starting from a host url and route plus adding a variable number of query params.
     * @param url - the base url
     * @param queryParams - mp of query params having a variable number of params.
     * @return - the constructed URL
     */
    static public String buildUrlWithQueryParams(String url, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder(url);

        if (queryParams != null) {
            sb.append("?");
            queryParams.forEach( (k, v) -> sb.append("&").append(k).append("=").append(v));
        }

        return sb.toString();
    }
}
