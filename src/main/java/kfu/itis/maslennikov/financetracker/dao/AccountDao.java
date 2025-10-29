package kfu.itis.maslennikov.financetracker.dao;

import kfu.itis.maslennikov.financetracker.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountDao {

    Optional<Account> findById(Long id);

    List<Account> findByUserId(Long userId);

    Long create(Account account);

    boolean update(Account account);

    boolean delete(Long id);

    boolean updateBalance(Long accountId, BigDecimal newBalance);
}