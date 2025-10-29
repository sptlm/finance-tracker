package kfu.itis.maslennikov.financetracker.dao;

import kfu.itis.maslennikov.financetracker.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    Optional<Tag> findById(Long id);

    List<Tag> findByUserId(Long userId);

    Long create(Tag tag);

    boolean update(Tag tag);

    boolean delete(Long id);
}
