package kfu.itis.maslennikov.financetracker.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Currency {
    
    private Long id;
    private String code;
    private String name;
    private String symbol;
    private BigDecimal exchangeRateToRub;

    public Currency() {
    }
    
    public Currency(Long id, String code, String name, String symbol,
                    BigDecimal exchangeRateToRub) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.exchangeRateToRub = exchangeRateToRub;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public BigDecimal getExchangeRateToRub() {
        return exchangeRateToRub;
    }
    
    public void setExchangeRateToRub(BigDecimal exchangeRateToRub) {
        this.exchangeRateToRub = exchangeRateToRub;
    }

}