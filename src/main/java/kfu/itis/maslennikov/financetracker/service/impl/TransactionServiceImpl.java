package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.TagDao;
import kfu.itis.maslennikov.financetracker.dao.TransactionDao;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.entity.Transaction;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.service.TransactionService;
import kfu.itis.maslennikov.financetracker.util.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionDao transactionDao;
    private final TagDao tagDao;

    public TransactionServiceImpl(TransactionDao transactionDao, TagDao tagDao) {
        this.transactionDao = transactionDao;
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionDao.findById(id);
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        return transactionDao.findByAccountId(accountId);
    }

    @Override
    public List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDate start, LocalDate end) {
        return transactionDao.findByAccountIdAndDateRange(accountId, start, end);
    }

    @Override
    public List<Transaction> findRecentByUserId(Long userId, int limit) {
        return transactionDao.findRecentByUserId(userId, limit);
    }

    @Override
    public Long create(Transaction transaction) {
        ValidationUtil.validateTransaction(transaction);
        
        return transactionDao.create(transaction);
    }

    @Override
    public boolean update(Transaction transaction) {
        if (transactionDao.findById(transaction.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Transaction not found with id: " + transaction.getId());
        }
        
        ValidationUtil.validateTransaction(transaction);
        
        return transactionDao.update(transaction);
    }

    @Override
    public boolean delete(Long id) {
        if (transactionDao.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
        
        return transactionDao.delete(id);
    }

    @Override
    public void addTagsToTransaction(Long transactionId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        
        for (Long tagId : tagIds) {
            transactionDao.addTagToTransaction(transactionId, tagId);
        }
    }

    @Override
    public void removeTagFromTransaction(Long transactionId, Long tagId) {
        transactionDao.removeTagFromTransaction(transactionId, tagId);
    }

    @Override
    public List<Long> getTagsForTransaction(Long transactionId) {
        return transactionDao.findTagIdsByTransaction(transactionId);
    }

    @Override
    public List<Transaction> filterTransactions(List<Transaction> list, String type, String categoryId,
                                                String[] tagIds, String dateFrom, String dateTo,
                                                String amountFrom, String amountTo) {
        Set<String> selectedTagSet = (tagIds == null || tagIds.length == 0) ?
                Collections.emptySet() :
                new HashSet<>(List.of(tagIds));
        return list.stream()
                .filter(t -> type == null || type.isEmpty() || type.equals(t.getType()))
                .filter(t -> categoryId == null || categoryId.isEmpty() || categoryId.equals(String.valueOf(t.getCategoryId())))
                .filter(t -> dateFrom == null || dateFrom.isEmpty() ||t.getTransactionDate().isAfter(LocalDate.parse(dateFrom)))
                .filter(t -> dateTo == null || dateTo.isEmpty() ||t.getTransactionDate().isBefore(LocalDate.parse(dateTo)))
                .filter(t -> amountFrom == null || amountFrom.isEmpty() || t.getAmount().compareTo(new BigDecimal(amountFrom)) >= 0)
                .filter(t -> amountTo == null || amountTo.isEmpty() || t.getAmount().compareTo(new BigDecimal(amountTo)) <= 0)
                .filter(t -> {
                    if (selectedTagSet.isEmpty()) return true;
                    java.util.List<Long> txTagIds = getTagsForTransaction(t.getId());
                    for (Long id : txTagIds) {
                        if (selectedTagSet.contains(id.toString())) return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Tag>> loadTagsForTransactions(List<Transaction> transactions) {
        Map<String, List<Tag>> tagsByTransaction = new HashMap<>();

        for (Transaction transaction : transactions) {
            List<Long> tagIds = transactionDao.findTagIdsByTransaction(transaction.getId());

            List<Tag> tags = new ArrayList<>();
            for (Long tagId : tagIds) {
                Optional<Tag> tagOpt = tagDao.findById(tagId);
                if (tagOpt.isPresent()) {
                    tags.add(tagOpt.get());
                }
            }
            tagsByTransaction.put(String.valueOf(transaction.getId()), tags);
        }
        return tagsByTransaction;
    }
}