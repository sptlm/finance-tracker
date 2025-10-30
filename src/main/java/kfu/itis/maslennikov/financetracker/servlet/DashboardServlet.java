package kfu.itis.maslennikov.financetracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Transaction;
import kfu.itis.maslennikov.financetracker.service.AccountService;
import kfu.itis.maslennikov.financetracker.service.StatisticsService;
import kfu.itis.maslennikov.financetracker.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private StatisticsService statisticsService;
    private TransactionService transactionService;
    private AccountService accountService;

    @Override
    public void init() {
        this.statisticsService = (StatisticsService) getServletContext().getAttribute("statisticsService");
        this.transactionService = (TransactionService) getServletContext().getAttribute("transactionService");
        this.accountService = (AccountService) getServletContext().getAttribute("accountService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();
        
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart = today.withDayOfYear(1);

        // Получаем все счета пользователя
        List<Account> accounts = accountService.findByUserId(userId);
        req.setAttribute("accounts", accounts);

        // Вычисляем общий баланс всех счетов
        BigDecimal totalBalance = accountService.getTotalBalance(userId);
        req.setAttribute("totalBalance", totalBalance);

        // Статистика за периоды
        // Сегодня
        BigDecimal todayIncome = statisticsService.getIncomeForPeriod(userId, today, today);
        BigDecimal todayExpense = statisticsService.getExpenseForPeriod(userId, today, today);
        req.setAttribute("todayIncome", todayIncome);
        req.setAttribute("todayExpense", todayExpense);
        req.setAttribute("todayBalance", todayIncome.subtract(todayExpense));
        
        // За неделю
        BigDecimal weekIncome = statisticsService.getIncomeForPeriod(userId, weekStart, today);
        BigDecimal weekExpense = statisticsService.getExpenseForPeriod(userId, weekStart, today);
        req.setAttribute("weekIncome", weekIncome);
        req.setAttribute("weekExpense", weekExpense);
        req.setAttribute("weekBalance", weekIncome.subtract(weekExpense));
        
        // За месяц
        BigDecimal monthIncome = statisticsService.getIncomeForPeriod(userId, monthStart, today);
        BigDecimal monthExpense = statisticsService.getExpenseForPeriod(userId, monthStart, today);
        req.setAttribute("monthIncome", monthIncome);
        req.setAttribute("monthExpense", monthExpense);
        req.setAttribute("monthBalance", monthIncome.subtract(monthExpense));
        
        // За год
        BigDecimal yearIncome = statisticsService.getIncomeForPeriod(userId, yearStart, today);
        BigDecimal yearExpense = statisticsService.getExpenseForPeriod(userId, yearStart, today);
        req.setAttribute("yearIncome", yearIncome);
        req.setAttribute("yearExpense", yearExpense);
        req.setAttribute("yearBalance", yearIncome.subtract(yearExpense));
        
        // Данные для Pie Charts (за текущий месяц)
        Map<String, BigDecimal> expensesByCategory = 
            statisticsService.getExpensesByCategory(userId, monthStart, today);
        Map<String, BigDecimal> incomeByCategory = 
            statisticsService.getIncomeByCategory(userId, monthStart, today);


        // Конвертируем в JSON для Chart.js
        ObjectMapper om = new ObjectMapper();
        req.setAttribute("expensesChartData", om.writeValueAsString(expensesByCategory));
        req.setAttribute("incomeChartData", om.writeValueAsString(incomeByCategory));
        
        // Последние транзакции
        List<Transaction> recentTransactions =
            transactionService.findRecentByUserId(userId, 10);
        req.setAttribute("recentTransactions", recentTransactions);

        req.getRequestDispatcher("/WEB-INF/views/dashboard.ftl").forward(req, resp);
    }
}