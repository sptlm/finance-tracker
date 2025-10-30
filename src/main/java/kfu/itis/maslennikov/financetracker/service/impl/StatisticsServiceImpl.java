package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.AccountDao;
import kfu.itis.maslennikov.financetracker.dao.CategoryDao;
import kfu.itis.maslennikov.financetracker.dao.TransactionDao;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.entity.Transaction;
import kfu.itis.maslennikov.financetracker.service.StatisticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsServiceImpl implements StatisticsService {
    
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final CategoryDao categoryDao;

    public StatisticsServiceImpl(TransactionDao transactionDao, AccountDao accountDao, CategoryDao categoryDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public BigDecimal getIncomeForPeriod(Long userId, LocalDate start, LocalDate end) {
        List<Account> userAccounts = accountDao.findByUserId(userId);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (Account account : userAccounts) {
            List<Transaction> transactions = transactionDao.findByAccountIdAndDateRange(
                account.getId(), start, end);
            
            BigDecimal accountIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            total = total.add(accountIncome);
        }
        
        return total;
    }

    @Override
    public BigDecimal getExpenseForPeriod(Long userId, LocalDate start, LocalDate end) {
        List<Account> userAccounts = accountDao.findByUserId(userId);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (Account account : userAccounts) {
            List<Transaction> transactions = transactionDao.findByAccountIdAndDateRange(
                account.getId(), start, end);
            
            BigDecimal accountExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            total = total.add(accountExpense);
        }
        
        return total;
    }

    @Override
    public BigDecimal getBalanceForPeriod(Long userId, LocalDate start, LocalDate end) {
        BigDecimal income = getIncomeForPeriod(userId, start, end);
        BigDecimal expense = getExpenseForPeriod(userId, start, end);
        return income.subtract(expense);
    }

    @Override
    public Map<String, BigDecimal> getExpensesByCategory(Long userId, LocalDate start, LocalDate end) {
        List<Account> userAccounts = accountDao.findByUserId(userId);
        List<Category> categories = categoryDao.findByUserId(userId);
        
        // Создаем map categoryId -> categoryName
        Map<Long, String> categoryNames = categories.stream()
            .collect(Collectors.toMap(Category::getId, Category::getName));
        
        Map<String, BigDecimal> result = new HashMap<>();
        
        for (Account account : userAccounts) {
            List<Transaction> transactions = transactionDao.findByAccountIdAndDateRange(
                account.getId(), start, end);
            
            transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .forEach(t -> {
                    String categoryName = categoryNames.getOrDefault(t.getCategoryId(), "Unknown");
                    result.merge(categoryName, t.getAmount(), BigDecimal::add);
                });
        }
        
        return result;
    }

    @Override
    public Map<String, BigDecimal> getIncomeByCategory(Long userId, LocalDate start, LocalDate end) {
        List<Account> userAccounts = accountDao.findByUserId(userId);
        List<Category> categories = categoryDao.findByUserId(userId);
        
        Map<Long, String> categoryNames = categories.stream()
            .collect(Collectors.toMap(Category::getId, Category::getName));
        
        Map<String, BigDecimal> result = new HashMap<>();
        
        for (Account account : userAccounts) {
            List<Transaction> transactions = transactionDao.findByAccountIdAndDateRange(
                account.getId(), start, end);
            
            transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .forEach(t -> {
                    String categoryName = categoryNames.getOrDefault(t.getCategoryId(), "Unknown");
                    result.merge(categoryName, t.getAmount(), BigDecimal::add);
                });
        }
        
        return result;
    }
}