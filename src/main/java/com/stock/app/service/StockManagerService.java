package com.stock.app.service;

import com.stock.app.model.StockDto;

public interface StockManagerService {

    StockDto getStock(String Symbol) throws Exception;
}
