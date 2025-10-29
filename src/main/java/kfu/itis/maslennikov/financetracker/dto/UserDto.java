package kfu.itis.maslennikov.financetracker.dto;

import kfu.itis.maslennikov.financetracker.entity.User;

public class UserDto {
    private final Long id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;

    public UserDto(Long id, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    // Геттеры
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
