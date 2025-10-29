package kfu.itis.maslennikov.financetracker.dao;

import kfu.itis.maslennikov.financetracker.entity.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionDao {

    Optional<Transaction> findById(Long id);

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDate startDate, LocalDate endDate);

    List<Transaction> findRecentByUserId(Long userId, int limit);

    boolean addTagToTransaction(Long transactionId, Long tagId);

    boolean removeTagFromTransaction(Long transactionId, Long tagId);

    List<Long> findTagIdsByTransaction(Long transactionId);

    List<Long> findTransactionIdsByTag(Long tagId);

    Long create(Transaction transaction);

    boolean update(Transaction transaction);

    boolean delete(Long id);
}
