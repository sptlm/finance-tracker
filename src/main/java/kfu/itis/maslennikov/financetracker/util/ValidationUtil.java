package kfu.itis.maslennikov.financetracker.util;

import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.entity.Transaction;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Za-zА-Яа-яё])(?=.*\\d)[A-Za-zА-Яа-яё\\d@$!%*#?&]{8,}$");
    private static final Pattern COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches() && email.length() <= 100;
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidName(String name) {
        return name == null || name.length() <= 50;
    }

    public static boolean isValidAccountName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    public static boolean isValidBalance(BigDecimal balance) {
        return balance != null && balance.compareTo(BigDecimal.ZERO) >= 0 && balance.compareTo(new BigDecimal(999_999_999_999_999L)) <= 0;
    }

    public static boolean isValidCategoryName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }

    public static boolean isValidCategoryType(String type) {
        return "INCOME".equals(type) || "EXPENSE".equals(type);
    }

    public static boolean isValidColor(String color) {
        return color == null || color.isEmpty() || COLOR_PATTERN.matcher(color).matches();
    }

    public static boolean isValidIcon(String icon) {
        return icon == null || icon.length() <= 50;
    }

    public static boolean isValidTransactionAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(new BigDecimal(999_999_999_999L)) <= 0;
    }

    public static boolean isValidTransactionType(String type) {
        return "INCOME".equals(type) || "EXPENSE".equals(type);
    }

    public static boolean isValidDescription(String description) {
        return description == null || description.length() <= 1000;
    }

    public static boolean isValidTagName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }


    public static void validateAccount(Account account) {
        if (account == null) {
            throw new ValidationException("Счёт не может быть null");
        }

        if (!isValidAccountName(account.getName())) {
            throw new ValidationException("Название счёта должно содержать от 1 до 100 символов");
        }

        if (!isValidId(account.getCurrencyId())) {
            throw new ValidationException("ID валюты должен быть больше 0");
        }

        if (!isValidBalance(account.getInitialBalance())) {
            throw new ValidationException("Начальный баланс не может быть отрицательным или больше 1 квадриллиона");
        }

        if (!isValidBalance(account.getCurrentBalance())) {
            throw new ValidationException("Текущий баланс не может быть отрицательным или больше 1 квадриллиона");
        }
    }

    public static void validateCategory(Category category) {
        if (category == null) {
            throw new ValidationException("Категория не может быть null");
        }

        if (!isValidCategoryName(category.getName())) {
            throw new ValidationException("Название категории должно содержать от 1 до 50 символов");
        }

        if (!isValidCategoryType(category.getType())) {
            throw new ValidationException("Тип категории должен быть INCOME или EXPENSE");
        }

        if (!isValidColor(category.getColor())) {
            throw new ValidationException("Цвет должен быть в формате #RRGGBB");
        }

        if (!isValidIcon(category.getIcon())) {
            throw new ValidationException("Иконка должна содержать от 0 до 50 символов");
        }
    }

    public static void validateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new ValidationException("Транзакция не может быть null");
        }

        if (transaction.getAmount() == null ||
                !isValidTransactionAmount(transaction.getAmount())) {
            throw new ValidationException("Сумма транзакции должна быть больше нуля и меньше 1 триллиона");
        }

        if (!isValidTransactionType(transaction.getType())) {
            throw new ValidationException("Тип транзакции должен быть INCOME или EXPENSE");
        }

        if (transaction.getTransactionDate() == null) {
            throw new ValidationException("Дата транзакции не может быть null");
        }

        if (!isValidId(transaction.getAccountId())) {
            throw new ValidationException("ID счёта не может быть null или 0");
        }

        if (!isValidId(transaction.getCategoryId())) {
            throw new ValidationException("ID категории не может быть null или 0");
        }

        if (!isValidDescription(transaction.getDescription())) {
            throw new ValidationException("Описание должно содержать от 0 до 1000 символов");
        }
    }

    public static void validateTag(Tag tag) {
        if (tag == null) {
            throw new ValidationException("Тег не может быть null");
        }

        if (!isValidTagName(tag.getName())) {
            throw new ValidationException("Название тега должно содержать от 1 до 50 символов");
        }

        if (!isValidColor(tag.getColor())) {
            throw new ValidationException("Цвет должен быть в формате #RRGGBB");
        }
    }
}