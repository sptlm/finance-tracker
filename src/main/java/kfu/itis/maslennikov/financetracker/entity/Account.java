package kfu.itis.maslennikov.financetracker.entity;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private Long userId;
    private String name;
    private String currency;
    private BigDecimal initialBalance;
    private BigDecimal  currentBalance;

    public Account() {}

    public Account(Long id, Long userId, String name, String currency, BigDecimal  initialBalance, BigDecimal  currentBalance) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.currency = currency;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal  getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal  initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal  getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal  currentBalance) {
        this.currentBalance = currentBalance;
    }
}
