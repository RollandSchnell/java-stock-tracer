package com.stock.app.service;

import com.stock.app.model.Stock;

public interface StockManagerService {

    Stock getStock(String Symbol) throws Exception;
}
