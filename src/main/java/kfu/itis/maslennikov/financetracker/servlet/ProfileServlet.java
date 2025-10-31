package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.User;
import kfu.itis.maslennikov.financetracker.service.UserService;
import kfu.itis.maslennikov.financetracker.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        
        req.setAttribute("user", userDto);

        String edit = req.getParameter("edit");
        req.setAttribute("editMode", "true".equals(edit));

        req.getRequestDispatcher("/WEB-INF/views/profile.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");

        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!ValidationUtil.isValidUsername(username)) {
            req.setAttribute("errorMessage",
                    "Имя пользователя должно содержать 3-50 символов (буквы, цифры, _)");
            req.getRequestDispatcher("/WEB-INF/views/profile.ftl").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidUsername(username)) {
            req.setAttribute("errorMessage",
                    "Имя и Фамилия должны содержать 3-50 символов (буквы, цифры, _)");
            req.getRequestDispatcher("/WEB-INF/views/profile.ftl").forward(req, resp);
            return;
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!ValidationUtil.isValidPassword(newPassword)) {
                req.setAttribute("errorMessage",
                        "Пароль должен содержать минимум 8 символов, включая буквы и цифры");
                req.getRequestDispatcher("/WEB-INF/views/profile.ftl").forward(req, resp);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("errorMessage", "Пароли не совпадают");
                req.getRequestDispatcher("/WEB-INF/views/profile.ftl").forward(req, resp);
                return;
            }
        }
        try {
            User updatedUser = new User(
                userDto.getId(),
                username,
                userDto.getEmail(),
                newPassword != null && !newPassword.isEmpty() ? newPassword : null,
                firstName,
                lastName
            );
            
            userService.update(updatedUser);
            
            UserDto updatedUserDto = new UserDto(updatedUser);
            req.getSession().setAttribute("user", updatedUserDto);

            req.getSession().setAttribute("successMessage", "Профиль успешно обновлен");
            resp.sendRedirect(req.getContextPath() + "/profile");
            
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Ошибка при обновлении профиля: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}