package com.stock.app.model;

import javax.persistence.*;

@Entity
@Table(name = "RULE")
public class RuleEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "min_stock_value")
    private Double minStockValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public RuleEntity() { }

    public RuleEntity(Long id, String stockName, Double minStockValue, String email) {
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

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public String toString() {
        return "id " + this.id + " stockName " + this.stockName + " minStockValue " + this.minStockValue;
    }

}
