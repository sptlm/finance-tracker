package kfu.itis.maslennikov.financetracker.exception;

public class ValidationException extends RuntimeException  {
    public ValidationException(String message) {
        super(message);
    }
}