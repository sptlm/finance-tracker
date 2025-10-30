package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.UserDao;
import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.User;
import kfu.itis.maslennikov.financetracker.exception.AuthenticationException;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.UserAlreadyExistsException;
import kfu.itis.maslennikov.financetracker.service.UserService;
import kfu.itis.maslennikov.financetracker.util.PasswordUtil;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto authenticate(String username, String password) {
        Optional<User> userOpt = userDao.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new AuthenticationException("User not found. Invalid username or password");
        }

        User user = userOpt.get();

        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return new UserDto(user);
    }

    @Override
    public UserDto register(User user, String password) {
        // Проверка уникальности username
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }

        // Проверка уникальности email
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        // Шифрование пароля вашим методом
        String encryptedPassword = PasswordUtil.encrypt(password);
        user.setPasswordHash(encryptedPassword);

        // Создание пользователя
        Long userId = userDao.create(user);
        user.setId(userId);

        return new UserDto(user);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()){
            return Optional.of(new UserDto(user.get()));
        }
        return Optional.empty();
    }

    @Override
    public boolean updateProfile(Long userId, String firstName, String lastName, String email) {
        Optional<User> userOpt = userDao.findById(userId);

        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        User user = userOpt.get();

        // Проверка email на уникальность, если он изменился
        if (!user.getEmail().equals(email)) {
            Optional<User> existingUser = userDao.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new UserAlreadyExistsException("Email already exists: " + email);
            }
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        return userDao.update(user);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userDao.findById(userId);

        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        User user = userOpt.get();

        // Проверка старого пароля
        if (!PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            throw new AuthenticationException("Old password is incorrect");
        }

        // Установка нового пароля
        String newEncryptedPassword = PasswordUtil.encrypt(newPassword);
        user.setPasswordHash(newEncryptedPassword);

        return userDao.update(user);
    }
}
