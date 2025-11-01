package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.AccountDao;
import kfu.itis.maslennikov.financetracker.dao.CurrencyDao;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;
import kfu.itis.maslennikov.financetracker.service.AccountService;
import kfu.itis.maslennikov.financetracker.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    
    private final AccountDao accountDao;
    private final CurrencyDao currencyDao;

    public AccountServiceImpl(AccountDao accountDao, CurrencyDao currencyDao) {
        this.accountDao = accountDao;
        this.currencyDao = currencyDao;
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
        if (account.getInitialBalance() == null) {
            account.setInitialBalance(BigDecimal.ZERO);
        }
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(account.getInitialBalance());
        }

        ValidationUtil.validateAccount(account);
        return accountDao.create(account);
    }

    @Override
    public boolean update(Account account, Long userId) {
        Optional<Account> accountOpt = accountDao.findById(account.getId());
        if (accountOpt.isEmpty()) {
            throw new ResourceNotFoundException("Аккаунт не найден с таким id: " + account.getId());
        }
        if (!accountOpt.get().getUserId().equals(userId)) {
            throw new ValidationException("У вас нет прав на изменение этого аккаунта");
        }
        ValidationUtil.validateAccount(account);
        return accountDao.update(account);
    }

    @Override
    public boolean delete(Long id, Long userId) {
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

        BigDecimal result = new BigDecimal(0);
        for (Account account : accounts) {
            BigDecimal balance = account.getCurrentBalance() != null ? account.getCurrentBalance() : BigDecimal.ZERO;
            result = result.add(
                    balance.multiply(
                            currencyDao.findById(account.getCurrencyId()).get().getExchangeRateToRub()
                    )
            );
        }
        return result;
    }
}