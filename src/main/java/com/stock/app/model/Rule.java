package com.stock.app.model;

import javax.persistence.*;

@Entity
@Table(name = "RULE")
public class Rule {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "min_stock_value")
    private Double minStockValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Rule () {

    }

    public Rule(Long id, String stockName, Double minStockValue, String email) {
        this.id = id;
        this.stockName = stockName;
        this.minStockValue = minStockValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getMinStockValue() {
        return minStockValue;
    }

    public void setMinStockValue(Double minStockValue) {
        this.minStockValue = minStockValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Rule[ID: %d, stockName: %s, minValue: %s]", id, stockName, minStockValue);
    }

}
