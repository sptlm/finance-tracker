package kfu.itis.maslennikov.financetracker.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private Long id;
    private Long accountId;
    private Long categoryId;
    private BigDecimal  amount;
    private String type; // INCOME или EXPENSE
    private String description;
    private LocalDate transactionDate;

    public Transaction(Long id, Long accountId, Long categoryId, BigDecimal  amount, String type, String description, LocalDate transactionDate) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
