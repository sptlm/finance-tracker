package kfu.itis.maslennikov.financetracker.service;

import kfu.itis.maslennikov.financetracker.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findById(Long id);

    List<Account> findByUserId(Long userId);

    Long create(Account account);

    boolean update(Account account, Long userId);

    boolean delete(Long id, Long userId); // userId для проверки прав

    BigDecimal getTotalBalance(Long userId);
}