package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.TagDao;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagDaoImpl implements TagDao {

    @Override
    public Optional<Tag> findById(Long id) {
        String sql = "SELECT id, user_id, name, color FROM tags WHERE id = ?";
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rsToTag(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding tag by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Tag> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, name, color FROM tags WHERE user_id = ? ORDER BY name";
        List<Tag> tags = new ArrayList<>();
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tags.add(rsToTag(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding tags by user", e);
        }
        return tags;
    }

    @Override
    public Long create(Tag tag) {
        String sql = "INSERT INTO tags (user_id, name, color) VALUES (?, ?, ?) RETURNING id";
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, tag.getUserId());
            ps.setString(2, tag.getName());
            ps.setString(3, tag.getColor());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tag", e);
        }
        throw new RuntimeException("Failed to create tag");
    }

    @Override
    public boolean update(Tag tag) {
        String sql = "UPDATE tags SET name = ?, color = ? WHERE id = ?";
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tag.getName());
            ps.setString(2, tag.getColor());
            ps.setLong(3, tag.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating tag", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM tags WHERE id = ?";
        try (Connection c = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting tag", e);
        }
    }

    private Tag rsToTag(ResultSet rs) throws SQLException {
        return new Tag(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("name"),
            rs.getString("color")
        );
    }
}