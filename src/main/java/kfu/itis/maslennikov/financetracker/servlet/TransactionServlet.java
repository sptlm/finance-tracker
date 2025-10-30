package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.entity.Transaction;
import kfu.itis.maslennikov.financetracker.service.AccountService;
import kfu.itis.maslennikov.financetracker.service.CategoryService;
import kfu.itis.maslennikov.financetracker.service.TagService;
import kfu.itis.maslennikov.financetracker.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    private TransactionService transactionService;
    private AccountService accountService;
    private CategoryService categoryService;
    private TagService tagService;

    @Override
    public void init() {
        this.transactionService = (TransactionService) getServletContext().getAttribute("transactionService");
        this.accountService     = (AccountService) getServletContext().getAttribute("accountService");
        this.categoryService    = (CategoryService) getServletContext().getAttribute("categoryService");
        this.tagService         = (TagService) getServletContext().getAttribute("tagService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();

        String type = req.getParameter("type");
        String category = req.getParameter("category");
        String[] tagIds = req.getParameterValues("tagIds");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo   = req.getParameter("dateTo");
        String amountFrom = req.getParameter("amountFrom");
        String amountTo   = req.getParameter("amountTo");

        List<Account> accounts = accountService.findByUserId(userId);
        List<Transaction> all = new ArrayList<>();
        for (Account a : accounts) all.addAll(transactionService.findByAccountId(a.getId()));

        List<Transaction> filtered = transactionService.filterTransactions(all, type, category, tagIds,
                dateFrom, dateTo, amountFrom, amountTo);

        List<Category> categories = categoryService.findByUserId(userId);
        List<Tag> tags = tagService.findByUserId(userId);

        Map<String, List<Tag>> tagsByTransaction = transactionService.loadTagsForTransactions(filtered);


        req.setAttribute("transactions", filtered);
        req.setAttribute("accounts", accounts);
        req.setAttribute("categories", categories);
        req.setAttribute("tags", tags);
        req.setAttribute("tagsByTransaction", tagsByTransaction);

        req.setAttribute("currentType", type == null ? "" : type);
        req.setAttribute("currentCategory", category == null ? "" : category);
        req.setAttribute("currentTags", tagIds == null ? new String[0] : tagIds);
        req.setAttribute("dateFrom", dateFrom == null ? "" : dateFrom);
        req.setAttribute("dateTo",   dateTo   == null ? "" : dateTo);
        req.setAttribute("amountFrom", amountFrom == null ? "" : amountFrom);
        req.setAttribute("amountTo",   amountTo   == null ? "" : amountTo);

        req.getRequestDispatcher("/WEB-INF/views/transactions.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "create" -> {
                    handleCreate(req);
                    req.getSession().setAttribute("successMessage", "Транзакция создана");
                }
                case "update" -> {
                    handleUpdate(req);
                    req.getSession().setAttribute("successMessage", "Транзакция обновлена");
                }
                case "delete" -> {
                    handleDelete(req);
                    req.getSession().setAttribute("successMessage", "Транзакция удалена");
                }
                case null, default -> req.getSession().setAttribute("errorMessage", "Неизвестное действие");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/transactions");
    }

    private void handleCreate(HttpServletRequest req) {
        Long accountId = Long.parseLong(req.getParameter("accountId"));
        Long categoryId = Long.parseLong(req.getParameter("categoryId"));
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        String type = req.getParameter("type");
        String description = req.getParameter("description");
        LocalDate date = LocalDate.parse(req.getParameter("transactionDate"));

        Transaction t = new Transaction(null, accountId, categoryId, amount, type, description, date);
        Long id = transactionService.create(t);

        String[] tagIds = req.getParameterValues("tagIds");
        if (tagIds != null && tagIds.length > 0) {
            List<Long> tagIdList = Arrays.stream(tagIds).map(Long::parseLong).collect(Collectors.toList());
            transactionService.addTagsToTransaction(id, tagIdList);
        }
    }

    private void handleUpdate(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        Long accountId = Long.parseLong(req.getParameter("accountId"));
        Long categoryId = Long.parseLong(req.getParameter("categoryId"));
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        String type = req.getParameter("type");
        String description = req.getParameter("description");
        LocalDate date = LocalDate.parse(req.getParameter("transactionDate"));

        Transaction t = new Transaction(id, accountId, categoryId, amount, type, description, date);
        transactionService.update(t);
    }

    private void handleDelete(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        transactionService.delete(id);
    }
}