package kfu.itis.maslennikov.financetracker.dao;

import kfu.itis.maslennikov.financetracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> getAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Long create(User user);

    boolean update(User user);

    boolean delete(Long id);
}
