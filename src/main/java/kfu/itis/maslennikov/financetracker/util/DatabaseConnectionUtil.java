package kfu.itis.maslennikov.financetracker.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionUtil {

    private static HikariDataSource dataSource;

static {
    try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    AppConfig appConfig = AppConfigUtil.loadConfig();

    // Конфигурация HikariCP
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(appConfig.DBUrl());
    hikariConfig.setUsername(appConfig.DBUser());
    hikariConfig.setPassword(appConfig.DBPassword());

    // Настройки пула соединений для PostgreSQL
    hikariConfig.setMaximumPoolSize(Integer.parseInt("10"));
    hikariConfig.setMinimumIdle(Integer.parseInt("5"));
    hikariConfig.setConnectionTimeout(Long.parseLong("30000"));
    hikariConfig.setIdleTimeout(Long.parseLong("600000"));
    hikariConfig.setMaxLifetime(Long.parseLong("1800000"));

    dataSource = new HikariDataSource(hikariConfig);

}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
