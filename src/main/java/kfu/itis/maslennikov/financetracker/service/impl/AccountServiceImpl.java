package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.AccountDao;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;
import kfu.itis.maslennikov.financetracker.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    
    private final AccountDao accountDao;

    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountDao.findById(id);
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountDao.findByUserId(userId);
    }

    @Override
    public Long create(Account account) {
        if (account.getName() == null || account.getName().trim().isEmpty()) {
            throw new ValidationException("Имя аккаунта не может быть пустым");
        }
        
        if (account.getCurrency() == null || account.getCurrency().trim().isEmpty()) {
            throw new ValidationException("Валюта не может быть пустой");
        }
        
        // начальный баланс = текущему, если не установлен
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(account.getInitialBalance() != null 
                ? account.getInitialBalance() 
                : BigDecimal.ZERO);
        }
        
        return accountDao.create(account);
    }

    @Override
    public boolean update(Account account) {
        if (accountDao.findById(account.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Аккаунт не найден с таким id: " + account.getId());
        }
        
        return accountDao.update(account);
    }

    @Override
    public boolean delete(Long id, Long userId) {
        // Проверка, что счет принадлежит пользователю
        Optional<Account> accountOpt = accountDao.findById(id);
        
        if (accountOpt.isEmpty()) {
            throw new ResourceNotFoundException("Аккаунт не найден с таким id: " + id);
        }
        
        if (!accountOpt.get().getUserId().equals(userId)) {
            throw new ValidationException("У вас нет прав на удаление этого аккаунта");
        }
        
        return accountDao.delete(id);
    }

    @Override
    public BigDecimal getTotalBalance(Long userId) {
        List<Account> accounts = accountDao.findByUserId(userId);
        
        return accounts.stream()
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}