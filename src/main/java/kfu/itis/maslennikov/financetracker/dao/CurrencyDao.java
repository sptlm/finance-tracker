package kfu.itis.maslennikov.financetracker.dao;

import kfu.itis.maslennikov.financetracker.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    Optional<Currency> findById(Long id);

    Optional<Currency> findByCode(String code);

    List<Currency> findAll();

    boolean updateExchangeRate(Long currencyId, BigDecimal newRate);

    Long create(Currency currency);
}