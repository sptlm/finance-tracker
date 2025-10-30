package kfu.itis.maslennikov.financetracker.util;

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

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Transaction amount must be greater than zero");
        }

        if (!Arrays.asList("INCOME", "EXPENSE").contains(transaction.getType())) {
            throw new ValidationException("Transaction type must be INCOME or EXPENSE");
        }

        if (transaction.getTransactionDate() == null) {
            throw new ValidationException("Transaction date cannot be null");
        }

        if (transaction.getAccountId() == null) {
            throw new ValidationException("Account ID cannot be null");
        }

        if (transaction.getCategoryId() == null) {
            throw new ValidationException("Category ID cannot be null");
        }
    }
}