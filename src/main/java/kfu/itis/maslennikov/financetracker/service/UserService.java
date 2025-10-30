package kfu.itis.maslennikov.financetracker.service;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto authenticate(String username, String password);

    UserDto register(User user, String password);

    Optional<UserDto> findById(Long id);

    boolean updateProfile(Long userId, String firstName, String lastName, String email);

    boolean changePassword(Long userId, String oldPassword, String newPassword);
}
