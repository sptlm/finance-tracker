package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.AccountDao;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.util.DatabaseConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    @Override
    public Optional<Account> findById(Long id) {
        String sql = "SELECT id, user_id, name, currency, initial_balance, current_balance " +
                "FROM accounts WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, id);

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, name, currency, initial_balance, current_balance " +
                "FROM accounts WHERE user_id = ? ORDER BY id";

        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, userId);

            try (ResultSet rs = pStatement.executeQuery()) {
                while (rs.next()) {
                    accounts.add(rsToAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding accounts for user: " + userId, e);
        }
        return accounts;
    }

    @Override
    public Long create(Account account) {
        String sql = "INSERT INTO accounts (user_id, name, currency, initial_balance, current_balance) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, account.getUserId());
            pStatement.setString(2, account.getName());
            pStatement.setString(3, account.getCurrency());
            pStatement.setBigDecimal(4, account.getInitialBalance());
            pStatement.setBigDecimal(5, account.getCurrentBalance());

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating account", e);
        }
        throw new RuntimeException("Failed to create account, no ID returned");
    }

    @Override
    public boolean update(Account account) {
        String sql = "UPDATE accounts SET name = ?, currency = ?, " +
                "initial_balance = ?, current_balance = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setString(1, account.getName());
            pStatement.setString(2, account.getCurrency());
            pStatement.setBigDecimal(3, account.getInitialBalance());
            pStatement.setBigDecimal(4, account.getCurrentBalance());
            pStatement.setLong(5, account.getId());

            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating account", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, id);
            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting account", e);
        }
    }

    @Override
    public boolean updateBalance(Long accountId, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET current_balance = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setBigDecimal(1, newBalance);
            pStatement.setLong(2, accountId);

            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating account balance", e);
        }
    }
    
    private Account rsToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("currency"),
                rs.getBigDecimal("initial_balance"),
                rs.getBigDecimal("current_balance")
        );
    }
}