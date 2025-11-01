package kfu.itis.maslennikov.financetracker.listener;

import kfu.itis.maslennikov.financetracker.dao.AccountDao;
import kfu.itis.maslennikov.financetracker.dao.CategoryDao;
import kfu.itis.maslennikov.financetracker.dao.CurrencyDao;
import kfu.itis.maslennikov.financetracker.dao.TagDao;
import kfu.itis.maslennikov.financetracker.dao.TransactionDao;
import kfu.itis.maslennikov.financetracker.dao.UserDao;
import kfu.itis.maslennikov.financetracker.dao.impl.AccountDaoImpl;
import kfu.itis.maslennikov.financetracker.dao.impl.CategoryDaoImpl;
import kfu.itis.maslennikov.financetracker.dao.impl.CurrencyDaoImpl;
import kfu.itis.maslennikov.financetracker.dao.impl.TagDaoImpl;
import kfu.itis.maslennikov.financetracker.dao.impl.TransactionDaoImpl;
import kfu.itis.maslennikov.financetracker.dao.impl.UserDaoImpl;
import kfu.itis.maslennikov.financetracker.service.AccountService;
import kfu.itis.maslennikov.financetracker.service.CategoryService;
import kfu.itis.maslennikov.financetracker.service.CurrencyService;
import kfu.itis.maslennikov.financetracker.service.StatisticsService;
import kfu.itis.maslennikov.financetracker.service.TagService;
import kfu.itis.maslennikov.financetracker.service.TransactionService;
import kfu.itis.maslennikov.financetracker.service.UserService;
import kfu.itis.maslennikov.financetracker.service.impl.AccountServiceImpl;
import kfu.itis.maslennikov.financetracker.service.impl.CategoryServiceImpl;
import kfu.itis.maslennikov.financetracker.service.impl.CurrencyServiceImpl;
import kfu.itis.maslennikov.financetracker.service.impl.StatisticsServiceImpl;
import kfu.itis.maslennikov.financetracker.service.impl.TagServiceImpl;
import kfu.itis.maslennikov.financetracker.service.impl.TransactionServiceImpl;
import kfu.itis.maslennikov.financetracker.service.impl.UserServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        // Инициализация DAO слоя
        UserDao userDao = new UserDaoImpl();
        AccountDao accountDao = new AccountDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        TransactionDao transactionDao = new TransactionDaoImpl();
        TagDao tagDao = new TagDaoImpl();
        CurrencyDao currencyDao = new CurrencyDaoImpl();

        // Инициализация сервисного слоя
        UserService userService = new UserServiceImpl(userDao);
        AccountService accountService = new AccountServiceImpl(accountDao, currencyDao);
        CategoryService categoryService = new CategoryServiceImpl(categoryDao);
        TransactionService transactionService = new TransactionServiceImpl(transactionDao, tagDao, accountDao, categoryDao);
        StatisticsService statisticsService = new StatisticsServiceImpl(transactionDao, accountDao, categoryDao, currencyDao);
        TagService tagService = new TagServiceImpl(tagDao);
        CurrencyService currencyService = new CurrencyServiceImpl(currencyDao);

        context.setAttribute("userService", userService);
        context.setAttribute("accountService", accountService);
        context.setAttribute("categoryService", categoryService);
        context.setAttribute("transactionService", transactionService);
        context.setAttribute("statisticsService", statisticsService);
        context.setAttribute("tagService", tagService);
        context.setAttribute("currencyService", currencyService);

        context.setAttribute("contextPath", context.getContextPath());

        System.out.println("Application context initialized");
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
