package com.stock.app.service;

import com.stock.app.constants.Constants;
import com.stock.app.model.Stock;
import com.stock.app.util.DateUtils;
import com.stock.app.util.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class StockManagerServiceImpl implements StockManagerService {

    private final static Logger Log = LoggerFactory.getLogger(StockManagerServiceImpl.class);

    @Value("${stock.api.url}")
    private String stockApiUri;

    @Value(("${stock.api.key}"))
    private String stockApiKey;

    private Stock createStockObject(String stockJson) throws Exception {
        String[] responseArray = stockJson.split(System.getProperty("line.separator"));

        String symbol = responseArray[2].split(":")[1].replaceAll("[^a-zA-Z0-9]", "");
        String low = responseArray[5].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
        String high = responseArray[4].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
        String open = responseArray[3].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
        String price = responseArray[6].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
        String change = responseArray[10].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");

        try {
            double lowDouble = Double.parseDouble(low);
            double highDouble = Double.parseDouble(high);
            double openDouble = Double.parseDouble(open);
            double priceDouble = Double.parseDouble(price);
            double changeDouble = Double.parseDouble(change);

            Stock stockObject = new Stock(symbol, lowDouble, highDouble, openDouble, changeDouble, priceDouble);

            Log.info("Stock object {}", stockObject.toString());

            return stockObject;
        } catch (Exception e) {
            throw new Exception("Could not create stock object");
        }
    }

    @Override
    public Stock getStock(String symbol) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> queryParams = new HashMap<>();

        queryParams.put(Constants.STOCK_API_FUNCTION_PARAM, Constants.STOCK_API_FUNCTION_TYPE);
        queryParams.put(Constants.STOCK_API_KEY_PARAM, stockApiKey);
        queryParams.put(Constants.STOCK_API_SYMBOL_PARAM, symbol);

        String constructedStockApiUri = UriUtils.buildUrlWithQUeryParams(stockApiUri, queryParams);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Log.info("[{}] calling external endpoint {}", DateUtils.getTimestamp(), constructedStockApiUri);

        String response = restTemplate.exchange(constructedStockApiUri, HttpMethod.GET, entity, String.class).getBody();

        try {
            if (response != null && !response.equals("")) {
                return createStockObject(response);
            } else {
                throw new Exception("Empty response");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }


}
