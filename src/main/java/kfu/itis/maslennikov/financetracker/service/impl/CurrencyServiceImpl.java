package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.CurrencyDao;
import kfu.itis.maslennikov.financetracker.entity.Currency;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;
import kfu.itis.maslennikov.financetracker.service.CurrencyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDao currencyDao;

    // количество знаков после запятой для рассчетов
    private static final int ACCURACY = 2;

    public CurrencyServiceImpl(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    public Optional<Currency> findById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID валюты должен быть положительным числом");
        }

        return currencyDao.findById(id);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Код валюты не может быть пустым");
        }

        String normalizedCode = code.trim().toUpperCase();

        if (normalizedCode.length() != 3) {
            throw new ValidationException("Код валюты должен состоять из 3 символов");
        }

        return currencyDao.findByCode(normalizedCode);
    }

    @Override
    public List<Currency> findAll() {
        return currencyDao.findAll();
    }

    @Override
    public Long create(Currency currency) {
        if (currency == null) {
            throw new ValidationException("Объект валюты не может быть null");
        }

        if (currency.getCode() == null || currency.getCode().trim().isEmpty()) {
            throw new ValidationException("Код валюты обязателен");
        }

        if (currency.getName() == null || currency.getName().trim().isEmpty()) {
            throw new ValidationException("Название валюты обязательно");
        }

        if (currency.getSymbol() == null || currency.getSymbol().trim().isEmpty()) {
            throw new ValidationException("Символ валюты обязателен");
        }

        if (currency.getExchangeRateToRub() == null ||
                currency.getExchangeRateToRub().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Курс обмена должен быть положительным числом");
        }

        currency.setCode(currency.getCode().trim().toUpperCase());

        Optional<Currency> existing = findByCode(currency.getCode());
        if (existing.isPresent()) {
            throw new ValidationException("Валюта с кодом " + currency.getCode() + " уже существует");
        }

        return currencyDao.create(currency);
    }

    @Override
    public boolean update(Currency currency) {
        if (currency == null) {
            throw new ValidationException("Объект валюты не может быть null");
        }

        if (currency.getId() == null || currency.getId() <= 0) {
            throw new ValidationException("ID валюты должен быть указан");
        }

        if (currency.getCode() == null || currency.getCode().trim().isEmpty()) {
            throw new ValidationException("Код валюты обязателен");
        }

        if (currency.getName() == null || currency.getName().trim().isEmpty()) {
            throw new ValidationException("Название валюты обязательно");
        }

        if (currency.getSymbol() == null || currency.getSymbol().trim().isEmpty()) {
            throw new ValidationException("Символ валюты обязателен");
        }

        if (currency.getExchangeRateToRub() == null ||
                currency.getExchangeRateToRub().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Курс обмена должен быть положительным числом");
        }

        Optional<Currency> existingOpt = findById(currency.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Валюта с ID " + currency.getId() + " не найдена");
        }

        Currency existing = existingOpt.get();

        currency.setCode(currency.getCode().trim().toUpperCase());

        if (!existing.getCode().equals(currency.getCode())) {
            Optional<Currency> duplicate = findByCode(currency.getCode());
            if (duplicate.isPresent()) {
                throw new ValidationException("Валюта с кодом " + currency.getCode() + " уже существует");
            }
        }

        return currencyDao.updateExchangeRate(currency.getId(), currency.getExchangeRateToRub());
    }

    @Override
    public BigDecimal convertToRubles(BigDecimal amount, Long fromCurrencyId) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Сумма для конвертации должна быть неотрицательной");
        }

        if (fromCurrencyId == null || fromCurrencyId <= 0) {
            throw new ValidationException("ID валюты некорректен");
        }

        Optional<Currency> currencyOpt = findById(fromCurrencyId);
        if (currencyOpt.isEmpty()) {
            throw new ResourceNotFoundException("Валюта не найдена");
        }

        Currency fromCurrency = currencyOpt.get();

        return amount
                .multiply(fromCurrency.getExchangeRateToRub())
                .setScale(ACCURACY, RoundingMode.HALF_UP);
    }
}