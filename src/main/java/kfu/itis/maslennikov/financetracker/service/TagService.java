package kfu.itis.maslennikov.financetracker.service;

import kfu.itis.maslennikov.financetracker.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Optional<Tag> findById(Long id);

    List<Tag> findByUserId(Long userId);

    Long create(Tag tag);

    boolean update(Tag tag);

    boolean delete(Long id, Long userId);
}