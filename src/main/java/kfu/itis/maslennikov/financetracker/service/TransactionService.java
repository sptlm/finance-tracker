package kfu.itis.maslennikov.financetracker.service;

import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.entity.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionService {
    Optional<Transaction> findById(Long id);

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDate start, LocalDate end);

    List<Transaction> findRecentByUserId(Long userId, int limit);

    Long create(Transaction transaction);

    boolean update(Transaction transaction);

    boolean delete(Long id);
    
    // Методы для работы с тегами
    void addTagsToTransaction(Long transactionId, List<Long> tagIds);

    void removeTagFromTransaction(Long transactionId, Long tagId);

    List<Long> getTagsForTransaction(Long transactionId);

    List<Transaction> filterTransactions(List<Transaction> list, String type, String categoryId,
                                         String[] tagIds, String dateFrom, String dateTo,
                                         String amountFrom, String amountTo);

    Map<String, List<Tag>> loadTagsForTransactions(List<Transaction> transactions);
}