package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dao.CurrencyDao;
import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Currency;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.service.AccountService;
import kfu.itis.maslennikov.financetracker.service.CurrencyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/accounts")
public class AccountServlet extends HttpServlet {
    
    private AccountService accountService;
    private CurrencyService currencyService;
    @Override
    public void init() throws ServletException {
        this.accountService = (AccountService) getServletContext().getAttribute("accountService");
        this.currencyService = (CurrencyService) getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            UserDto user = (UserDto) req.getSession().getAttribute("user");

            Long userId = user.getId();

            List<Account> accounts = accountService.findByUserId(userId);
            BigDecimal totalBalance = accountService.getTotalBalance(userId);

            List<Currency> currencies = currencyService.findAll();

            req.setAttribute("accounts", accounts);
            req.setAttribute("totalBalance", totalBalance);
            req.setAttribute("currencies", currencies);

            req.getRequestDispatcher("/WEB-INF/views/accounts.ftl").forward(req, resp);

        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Ошибка при загрузке счётов" + Arrays.toString(e.getStackTrace()));
            req.getRequestDispatcher("/WEB-INF/views/accounts.ftl").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                handleCreate(req, user.getId());
                req.getSession().setAttribute("successMessage", "Счёт успешно создан");
            } else if ("update".equals(action)) {
                handleUpdate(req);
                req.getSession().setAttribute("successMessage", "Счёт успешно обновлён");
            } else if ("delete".equals(action)) {
                handleDelete(req);
                req.getSession().setAttribute("successMessage", "Счёт успешно удалён");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        
        // PRG
        resp.sendRedirect(req.getContextPath() + "/accounts");
    }
    
    private void handleCreate(HttpServletRequest req, Long userId) {
        String name = req.getParameter("name");
        Long currencyId = Long.parseLong(req.getParameter("currencyId"));
        BigDecimal initialBalance = new BigDecimal(req.getParameter("initialBalance"));
        
        Account account = new Account(null, userId,name,currencyId,initialBalance, initialBalance);
        
        accountService.create(account);
    }
    
    private void handleUpdate(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        Long currencyId = Long.parseLong(req.getParameter("currencyId"));
        
        Optional<Account> account = accountService.findById(id);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Аккаунт не найден с таким id: " + id);
        }
        account.get().setName(name);
        account.get().setCurrencyId(currencyId);
        
        accountService.update(account.get());
    }
    
    private void handleDelete(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();
        accountService.delete(id,userId);
    }
}