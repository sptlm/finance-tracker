package kfu.itis.maslennikov.financetracker.service;

import kfu.itis.maslennikov.financetracker.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    Optional<Currency> findById(Long id);

    Optional<Currency> findByCode(String code);

    List<Currency> findAll();

    Long create(Currency currency);

    boolean update(Currency currency);

    BigDecimal convertToRubles(BigDecimal amount, Long fromCurrencyId);
}