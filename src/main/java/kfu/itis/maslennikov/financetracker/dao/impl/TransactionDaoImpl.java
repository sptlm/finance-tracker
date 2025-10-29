package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.TransactionDao;
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
        String sql = "SELECT id, account_id, category_id, amount, type, description, transaction_date " +
                "FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setLong(1, id);
            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) return Optional.of(rsToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        String sql = "SELECT id, account_id, category_id, amount, type, description, transaction_date " +
                "FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC, id DESC";
        List<Transaction> result = new ArrayList<>();
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setLong(1, accountId);
            try (ResultSet rs = pStatement.executeQuery()) {
                while (rs.next()) result.add(rsToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by account_id", e);
        }
        return result;
    }

    @Override
    public List<Transaction> findByAccountIdAndDateRange(Long accountId, LocalDate start, LocalDate end) {
        String sql = "SELECT id, account_id, category_id, amount, type, description, transaction_date " +
                "FROM transactions WHERE account_id = ? AND transaction_date BETWEEN ? AND ? " +
                "ORDER BY transaction_date DESC";
        List<Transaction> result = new ArrayList<>();
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setLong(1, accountId);
            pStatement.setDate(2, Date.valueOf(start));
            pStatement.setDate(3, Date.valueOf(end));
            try (ResultSet rs = pStatement.executeQuery()) {
                while (rs.next()) result.add(rsToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions in date range", e);
        }
        return result;
    }
    @Override
    public List<Transaction> findRecentByUserId(Long userId, int limit) {
        String sql = """
                SELECT t.id, t.account_id, t.category_id, t.amount, t.type,
                       t.description, t.transaction_date
                FROM transactions t
                JOIN accounts a ON t.account_id = a.id
                WHERE a.user_id = ?
                ORDER BY t.transaction_date DESC, t.id DESC
                LIMIT ?
                """;

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
            throw new RuntimeException("Error retrieving recent transactions for user " + userId, e);
        }

        return transactions;
    }

    // M2M связи

    @Override
    public boolean addTagToTransaction(Long transactionId, Long tagId) {
        String sql = "INSERT INTO transaction_tags (transaction_id, tag_id) VALUES (?, ?) " +
                "ON CONFLICT DO NOTHING";
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
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
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
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
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tagIds.add(rs.getLong("tag_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading tags by transaction", e);
        }
        return tagIds;
    }

    @Override
    public List<Long> findTransactionIdsByTag(Long tagId) {
        String sql = "SELECT transaction_id FROM transaction_tags WHERE tag_id = ?";
        List<Long> transactionIds = new ArrayList<>();
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, tagId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) transactionIds.add(rs.getLong("transaction_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading transactions by tag", e);
        }
        return transactionIds;
    }


    @Override
    public Long create(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, category_id, amount, type, description, transaction_date) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setLong(1, transaction.getAccountId());
            pStatement.setLong(2, transaction.getCategoryId());
            pStatement.setBigDecimal(3, transaction.getAmount());
            pStatement.setString(4, transaction.getType());
            pStatement.setString(5, transaction.getDescription());
            pStatement.setDate(6, Date.valueOf(transaction.getTransactionDate()));
            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating transaction", e);
        }
        throw new RuntimeException("Failed to create transaction");
    }

    @Override
    public boolean update(Transaction transaction) {
        String sql = "UPDATE transactions SET account_id=?, category_id=?, amount=?, type=?, description=?, transaction_date=? WHERE id=?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setLong(1, transaction.getAccountId());
            pStatement.setLong(2, transaction.getCategoryId());
            pStatement.setBigDecimal(3, transaction.getAmount());
            pStatement.setString(4, transaction.getType());
            pStatement.setString(5, transaction.getDescription());
            pStatement.setDate(6, Date.valueOf(transaction.getTransactionDate()));
            pStatement.setLong(7, transaction.getId());
            return pStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM transactions WHERE id=?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setLong(1, id);
            return pStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }

    private Transaction rsToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
        rs.getLong("id"),
        rs.getLong("account_id"),
        rs.getLong("category_id"),
        rs.getBigDecimal("amount"),
        rs.getString("type"),
        rs.getString("description"),
        rs.getDate("transaction_date").toLocalDate()
        );
    }
}

