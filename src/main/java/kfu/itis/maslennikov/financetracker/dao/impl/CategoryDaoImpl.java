package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.CategoryDao;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "SELECT id, user_id, name, type, color, icon " +
                "FROM categories WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, id);

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, name, type, color, icon " +
                "FROM categories WHERE user_id = ? ORDER BY type, name";

        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, userId);

            try (ResultSet rs = pStatement.executeQuery()) {
                while (rs.next()) {
                    categories.add(rsToCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding categories for user: " + userId, e);
        }
        return categories;
    }

    @Override
    public List<Category> findByUserIdAndType(Long userId, String type) {
        String sql = "SELECT id, user_id, name, type, color, icon " +
                "FROM categories WHERE user_id = ? AND type = ? ORDER BY name";

        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, userId);
            pStatement.setString(2, type);

            try (ResultSet rs = pStatement.executeQuery()) {
                while (rs.next()) {
                    categories.add(rsToCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding categories by type for user: " + userId, e);
        }
        return categories;
    }

    @Override
    public Long create(Category category) {
        String sql = "INSERT INTO categories (user_id, name, type, color, icon) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, category.getUserId());
            pStatement.setString(2, category.getName());
            pStatement.setString(3, category.getType());
            pStatement.setString(4, category.getColor());
            pStatement.setString(5, category.getIcon());

            try (ResultSet rs = pStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating category", e);
        }
        throw new RuntimeException("Failed to create category, no ID returned");
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, type = ?, color = ?, icon = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setString(1, category.getName());
            pStatement.setString(2, category.getType());
            pStatement.setString(3, category.getColor());
            pStatement.setString(4, category.getIcon());
            pStatement.setLong(5, category.getId());

            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {

            pStatement.setLong(1, id);
            int rowsAffected = pStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    private Category rsToCategory(ResultSet rs) throws SQLException {
        return new Category(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getString("color"),
            rs.getString("icon")
        );
    }
}