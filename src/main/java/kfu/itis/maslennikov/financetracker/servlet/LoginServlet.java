package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.User;
import kfu.itis.maslennikov.financetracker.exception.AuthenticationException;
import kfu.itis.maslennikov.financetracker.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

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
        if (req.getParameter("registered") !=null && req.getParameter("registered").equals("true")) {
            req.setAttribute("successMessage", "Вы успешно зарегистрировались!");
        }
        req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String usernameOrEmail = req.getParameter("usernameOrEmail");
        String password = req.getParameter("password");

        // Валидация
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Укажите имя пользователя или email");
            req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp);
            return;
        }

        if (password == null || password.isEmpty()) {
            req.setAttribute("errorMessage", "Укажите пароль");
            req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp);
            return;
        }
        try {

            UserDto user = userService.authenticate(usernameOrEmail.trim(), password);
            HttpSession s = req.getSession();
            s.setAttribute("user", user);
            s.setMaxInactiveInterval(1800);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (AuthenticationException ex) {
            req.setAttribute("errorMessage", "Неверное имя пользователя или пароль");
            req.setAttribute("usernameOrEmail", usernameOrEmail);
            req.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(req, resp);
        }
    }
}