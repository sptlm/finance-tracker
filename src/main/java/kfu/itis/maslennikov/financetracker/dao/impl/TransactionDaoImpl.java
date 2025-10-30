package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.TransactionDao;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.entity.Currency;
import kfu.itis.maslennikov.financetracker.entity.Transaction;
import kfu.itis.maslennikov.financetracker.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TransactionDaoImpl implements TransactionDao {


    @Override
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT t.id, t.account_id, t.category_id, t.amount, t.type, t.description, t.transaction_date, " +
                "       a.user_id, a.name as account_name, a.currency_id, a.initial_balance, a.current_balance, " +
                "       c.id as curr_id, c.code, c.name as curr_name, c.symbol, c.exchange_rate_to_rub, " +
                "       cat.name as category_name, cat.user_id as cat_user_id, cat.id as cat_id, cat.color, cat.icon, cat.type as cat_type " +
                "FROM transactions t " +
                "JOIN accounts a ON t.account_id = a.id " +
                "JOIN currencies c ON a.currency_id = c.id " +
                "LEFT JOIN categories cat ON t.category_id = cat.id " +
                "WHERE t.id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction by id: " + id, e);
        }

        return Optional.empty();
    }


    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        String sql = "SELECT t.id, t.account_id, t.category_id, t.amount, t.type, t.description, t.transaction_date, " +
                "       a.user_id, a.name as account_name, a.currency_id, a.initial_balance, a.current_balance, " +
                "       c.id as curr_id, c.code, c.name as curr_name, c.symbol, c.exchange_rate_to_rub, " +
                "       cat.name as category_name, cat.user_id as cat_user_id, cat.id as cat_id, cat.color, cat.icon, cat.type as cat_type " +
                "FROM transactions t " +
                "JOIN accounts a ON t.account_id = a.id " +
                "JOIN currencies c ON a.currency_id = c.id " +
                "LEFT JOIN categories cat ON t.category_id = cat.id " +
                "WHERE t.account_id = ? " +
                "ORDER BY t.transaction_date DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(rsToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }

        return transactions;
    }


    @Override
    public List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDate start, LocalDate end) {
        String sql = "SELECT t.id, t.account_id, t.category_id, t.amount, t.type, t.description, t.transaction_date, " +
                "       a.user_id, a.name as account_name, a.currency_id, a.initial_balance, a.current_balance, " +
                "       c.id as curr_id, c.code, c.name as curr_name, c.symbol, c.exchange_rate_to_rub, " +
                "       cat.name as category_name, cat.user_id as cat_user_id, cat.id as cat_id, cat.color, cat.icon, cat.type as cat_type " +
                "FROM transactions t " +
                "JOIN accounts a ON t.account_id = a.id " +
                "JOIN currencies c ON a.currency_id = c.id " +
                "LEFT JOIN categories cat ON t.category_id = cat.id " +
                "WHERE t.account_id = ? AND t.transaction_date BETWEEN ? AND ? " +
                "ORDER BY t.transaction_date DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accountId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(rsToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by account and date range", e);
        }

        return transactions;
    }


    @Override
    public List<Transaction> findRecentByUserId(Long userId, int limit) {
        String sql = "SELECT t.id, t.account_id, t.category_id, t.amount, t.type, t.description, t.transaction_date, " +
                "       a.user_id, a.name as account_name, a.currency_id, a.initial_balance, a.current_balance, " +
                "       c.id as curr_id, c.code, c.name as curr_name, c.symbol, c.exchange_rate_to_rub, " +
                "       cat.name as category_name, cat.user_id as cat_user_id, cat.id as cat_id, cat.color, cat.icon, cat.type as cat_type " +
                "FROM transactions t " +
                "JOIN accounts a ON t.account_id = a.id " +
                "JOIN currencies c ON a.currency_id = c.id " +
                "LEFT JOIN categories cat ON t.category_id = cat.id " +
                "WHERE a.user_id = ? " +
                "ORDER BY t.transaction_date DESC " +
                "LIMIT ?";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(rsToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recent transactions", e);
        }

        return transactions;
    }

    @Override
    public Long create(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, category_id, amount, type, description, transaction_date) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, transaction.getAccountId());
            ps.setLong(2, transaction.getCategoryId());
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setString(4, transaction.getType());
            ps.setString(5, transaction.getDescription());
            ps.setDate(6, Date.valueOf(transaction.getTransactionDate()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating transaction", e);
        }

        throw new RuntimeException("Failed to create transaction, no ID returned");
    }

    @Override
    public boolean update(Transaction transaction) {
        String sql = "UPDATE transactions SET account_id = ?, category_id = ?, amount = ?, " +
                "type = ?, description = ?, transaction_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, transaction.getAccountId());
            ps.setLong(2, transaction.getCategoryId());
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setString(4, transaction.getType());
            ps.setString(5, transaction.getDescription());
            ps.setDate(6, Date.valueOf(transaction.getTransactionDate()));
            ps.setLong(7, transaction.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }

    // M2M связи с тегами

    @Override
    public boolean addTagToTransaction(Long transactionId, Long tagId) {
        String sql = "INSERT INTO transaction_tags (transaction_id, tag_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, transactionId);
            ps.setLong(2, tagId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding tag to transaction", e);
        }
    }

    @Override
    public boolean removeTagFromTransaction(Long transactionId, Long tagId) {
        String sql = "DELETE FROM transaction_tags WHERE transaction_id = ? AND tag_id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, transactionId);
            ps.setLong(2, tagId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error removing tag from transaction", e);
        }
    }

    @Override
    public List<Long> findTagIdsByTransaction(Long transactionId) {
        String sql = "SELECT tag_id FROM transaction_tags WHERE transaction_id = ?";
        List<Long> tagIds = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, transactionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tagIds.add(rs.getLong("tag_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding tags for transaction", e);
        }

        return tagIds;
    }

    @Override
    public List<Long> findTransactionIdsByTag(Long tagId) {
        String sql = "SELECT transaction_id FROM transaction_tags WHERE tag_id = ?";
        List<Long> transactionIds = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, tagId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactionIds.add(rs.getLong("transaction_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions for tag", e);
        }

        return transactionIds;
    }


    private Transaction rsToTransaction(ResultSet rs) throws SQLException {
        Currency currency = new Currency(
                rs.getLong("curr_id"),
                rs.getString("code"),
                rs.getString("curr_name"),
                rs.getString("symbol"),
                rs.getBigDecimal("exchange_rate_to_rub")
        );

        Account account = new Account(
                rs.getLong("account_id"),
                rs.getLong("user_id"),
                rs.getString("account_name"),
                rs.getLong("currency_id"),
                currency,
                rs.getBigDecimal("initial_balance"),
                rs.getBigDecimal("current_balance")
        );

        // Создаем Category если есть (LEFT JOIN может вернуть null)
        Category category = null;
        Long categoryId = rs.getLong("category_id");
        if (!rs.wasNull()) {
            category = new Category(
                    categoryId,
                    rs.getLong("cat_user_id"),
                    rs.getString("category_name"),
                    rs.getString("cat_type"),
                    rs.getString("color"),
                    rs.getString("icon")
            );
        }

        return new Transaction(
                rs.getLong("id"),
                rs.getLong("account_id"),
                categoryId,
                rs.getBigDecimal("amount"),
                rs.getString("type"),
                rs.getString("description"),
                rs.getDate("transaction_date").toLocalDate(),
                account,
                category
        );
    }
}

