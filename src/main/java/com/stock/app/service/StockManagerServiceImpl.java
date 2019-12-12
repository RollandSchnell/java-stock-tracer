package com.stock.app.service;

import com.stock.app.constants.Constants;
import com.stock.app.model.StockDto;
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

/**
 * Service used to create a stock object based on the external stock API response, uses the Stock model as a DTO, just for
 * data manipulation not as a db Entity.
 *
 * @author rolland.schnell
 */
@Service
public class StockManagerServiceImpl implements StockManagerService {

    private final static Logger Log = LoggerFactory.getLogger(StockManagerServiceImpl.class);

    @Value("${stock.api.url}")
    private String stockApiUri;

    @Value("${stock.api.key}")
    private String stockApiKey;

    /**
     * Using the stock response in JSON format from the external stock provider API create the stock object model. Save just
     * the relevant data from the response, such as, the daily min, max, average, open value the percentage changed from opening to
     * closing and the stock symbol value requested.
     *
     * @param stockJson - the returned JSON string from the stock API
     * @return - parsed stock object
     * @throws Exception - if the stock object could not be created
     */
    private StockDto createStockObject(String stockJson) throws Exception {
        try {
            String[] responseArray = stockJson.split(System.getProperty("line.separator"));

            String symbol = responseArray[2].split(":")[1].replaceAll("[^a-zA-Z0-9]", "");
            String low = responseArray[5].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
            String high = responseArray[4].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
            String open = responseArray[3].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
            String price = responseArray[6].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");
            String change = responseArray[10].split(":")[1].replaceAll("[^.a-zA-Z0-9]", "");

            double lowDouble = Double.parseDouble(low);
            double highDouble = Double.parseDouble(high);
            double openDouble = Double.parseDouble(open);
            double priceDouble = Double.parseDouble(price);
            double changeDouble = Double.parseDouble(change);

            StockDto stockObject = new StockDto(symbol, lowDouble, highDouble, openDouble, changeDouble, priceDouble);

            Log.info("[{}] Stock object {}", stockObject.toString(), DateUtils.getTimestamp());

            return stockObject;
        } catch (Exception e) {
            throw new Exception("Could not create stock object");
        }
    }

    /**
     * Executes the actual external API call to retrieve stock information filtered by a given stock symbol, and returns
     * the parsed stock object.
     *
     * @param symbol - the stock symbol for what the data is requested
     * @return - parsed Stock object
     * @throws Exception - if the call fails or the stock object could not be created.
     */
    @Override
    public StockDto getStock(String symbol) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> queryParams = new HashMap<>();

        queryParams.put(Constants.STOCK_API_FUNCTION_PARAM, Constants.STOCK_API_FUNCTION_TYPE);
        queryParams.put(Constants.STOCK_API_KEY_PARAM, stockApiKey);
        queryParams.put(Constants.STOCK_API_SYMBOL_PARAM, symbol);

        String constructedStockApiUri = UriUtils.buildUrlWithQueryParams(stockApiUri, queryParams);
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
