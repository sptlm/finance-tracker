package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.UserDao;
import kfu.itis.maslennikov.financetracker.entity.User;
import kfu.itis.maslennikov.financetracker.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public List<User> getAll() {
        final String sql = "SELECT id, username, email, password_hash, first_name, last_name FROM users ORDER BY id";

        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    users.add(rsToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load users list", e);
        }

        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, username, email, password_hash, first_name, last_name " +
                "FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, id);

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, email, password_hash, first_name, last_name " +
                "FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setString(1, username);

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username: " + username, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, username, email, password_hash, first_name, last_name " +
                "FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setString(1, email);

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email: " + email, e);
        }
        return Optional.empty();
    }

    @Override
    public Long create(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, first_name, last_name) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setString(1, user.getUsername());
            pStatement.setString(2, user.getEmail());
            pStatement.setString(3, user.getPasswordHash());
            pStatement.setString(4, user.getFirstName());
            pStatement.setString(5, user.getLastName());

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
        throw new RuntimeException("Failed to create user, no ID returned");
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, first_name = ?, last_name = ?" +
                " WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setString(1, user.getUsername());
            pStatement.setString(2, user.getEmail());
            pStatement.setString(3, user.getFirstName());
            pStatement.setString(4, user.getLastName());
            pStatement.setLong(5, user.getId());

            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, id);
            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }


    private User rsToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}
