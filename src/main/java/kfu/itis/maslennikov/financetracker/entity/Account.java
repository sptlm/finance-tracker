package kfu.itis.maslennikov.financetracker.entity;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private Long userId;
    private String name;
    private Long currencyId;
    private Currency currency;
    private BigDecimal initialBalance;
    private BigDecimal  currentBalance;

    public Account() {}

    public Account(Long id, Long userId, String name, Long currencyId, BigDecimal  initialBalance, BigDecimal  currentBalance) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.currencyId = currencyId;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
    }

    public Account(Long id, Long userId, String name, Long currencyId, Currency currency, BigDecimal initialBalance, BigDecimal currentBalance) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.currencyId = currencyId;
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

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    // для передачи данных на шаблоны

    public String getCurrencyCode() {
        return currency != null ? currency.getCode() : "RUB";
    }

    public String getCurrencySymbol() {
        return currency != null ? currency.getSymbol() : "₽";
    }

    public BigDecimal getExchangeRateToRub() {
        return currency != null ? currency.getExchangeRateToRub() : BigDecimal.ONE;
    }

    public BigDecimal getCurrentBalanceInRub() {
        if (currentBalance == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal exchangeRate = getExchangeRateToRub();
        return currentBalance.multiply(exchangeRate);
    }
}
