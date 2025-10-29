package kfu.itis.maslennikov.financetracker.dao;

import kfu.itis.maslennikov.financetracker.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    Optional<Category> findById(Long id);

    List<Category> findByUserId(Long userId);

    List<Category> findByUserIdAndType(Long userId, String type);

    Long create(Category category);

    boolean update(Category category);

    boolean delete(Long id);
}
