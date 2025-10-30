package kfu.itis.maslennikov.financetracker.dao.impl;

import kfu.itis.maslennikov.financetracker.dao.CurrencyDao;
import kfu.itis.maslennikov.financetracker.entity.Currency;
import kfu.itis.maslennikov.financetracker.util.DatabaseConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {
    
    @Override
    public Optional<Currency> findById(Long id) {
        String sql = "SELECT id, code, name, symbol, exchange_rate_to_rub " +
                     "FROM currencies WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToCurrency(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding currency by id: " + id, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Currency> findByCode(String code) {
        String sql = "SELECT id, code, name, symbol, exchange_rate_to_rub " +
                     "FROM currencies WHERE code = ?";
        
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, code);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rsToCurrency(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding currency by code: " + code, e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Currency> findAll() {
        String sql = "SELECT id, code, name, symbol, exchange_rate_to_rub " +
                     "FROM currencies";
        
        List<Currency> currencies = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                currencies.add(rsToCurrency(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all currencies", e);
        }
        
        return currencies;
    }
    
    @Override
    public boolean updateExchangeRate(Long currencyId, BigDecimal newRate) {
        String sql = "UPDATE currencies SET exchange_rate_to_rub = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, newRate);
            ps.setLong(2, currencyId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating exchange rate for currency: " + currencyId, e);
        }
    }
    
    @Override
    public Long create(Currency currency) {
        String sql = "INSERT INTO currencies (code, name, symbol, exchange_rate_to_rub) " +
                     "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getName());
            ps.setString(3, currency.getSymbol());
            ps.setBigDecimal(4, currency.getExchangeRateToRub());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error creating currency: " + currency.getCode(), e);
        }
        
        return null;
    }
    

    private Currency rsToCurrency(ResultSet rs) throws SQLException {
        return new Currency(
            rs.getLong("id"),
            rs.getString("code"),
            rs.getString("name"),
            rs.getString("symbol"),
            rs.getBigDecimal("exchange_rate_to_rub")
        );
    }
}