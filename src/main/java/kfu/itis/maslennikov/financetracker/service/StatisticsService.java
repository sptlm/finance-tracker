package kfu.itis.maslennikov.financetracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {
    BigDecimal getIncomeForPeriod(Long userId, LocalDate start, LocalDate end);

    BigDecimal getExpenseForPeriod(Long userId, LocalDate start, LocalDate end);

    BigDecimal getBalanceForPeriod(Long userId, LocalDate start, LocalDate end);

    Map<String, BigDecimal> getExpensesByCategory(Long userId, LocalDate start, LocalDate end);

    Map<String, BigDecimal> getIncomeByCategory(Long userId, LocalDate start, LocalDate end);
}