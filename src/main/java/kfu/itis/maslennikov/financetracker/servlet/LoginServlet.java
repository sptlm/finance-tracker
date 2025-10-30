package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.exception.AuthenticationException;
import kfu.itis.maslennikov.financetracker.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext()
                .getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp); // forward к FreemarkerServlet [web:12]
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String u = req.getParameter("username");
        String p = req.getParameter("password");
        if (u == null || u.isBlank() || p == null || p.isBlank()) {
            req.setAttribute("errorMessage", "Введите имя пользователя и пароль");// request атрибут [комментарий]
            req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp); // показать ошибку [web:12]
            return;
        }
        try {
            UserDto user = userService.authenticate(u.trim(), p); // бизнес‑логика [комментарий]
            HttpSession s = req.getSession();
            s.setAttribute("user", user); // держим минимальные данные (DTO без пароля) [комментарий]
            s.setMaxInactiveInterval(1800);
            resp.sendRedirect(req.getContextPath() + "/dashboard"); // PRG редирект [web:151]
        } catch (AuthenticationException ex) {
            req.setAttribute("errorMessage", "Неверное имя пользователя или пароль");
            req.setAttribute("username", u);
            req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp); // остаёмся на форме [web:12]
        }
    }
}