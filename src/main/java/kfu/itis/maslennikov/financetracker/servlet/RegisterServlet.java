package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.User;
import kfu.itis.maslennikov.financetracker.exception.UserAlreadyExistsException;
import kfu.itis.maslennikov.financetracker.service.CategoryService;
import kfu.itis.maslennikov.financetracker.service.UserService;
import kfu.itis.maslennikov.financetracker.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UserService userService;
    private CategoryService categoryService;

    @Override
    public void init(){
        this.userService = (UserService) getServletContext().getAttribute("userService");;
        this.categoryService = (CategoryService) getServletContext().getAttribute("categoryService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        
        // Сохраняем введённые данные для отображения при ошибке
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("firstName", firstName);
        req.setAttribute("lastName", lastName);

        if (!ValidationUtil.isValidUsername(username)) {
            req.setAttribute("errorMessage", 
                "Имя пользователя должно содержать 3-50 символов (буквы, цифры, _)");
            req.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(req, resp);
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("errorMessage", "Введите корректный email");
            req.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(req, resp);
            return;
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("errorMessage", 
                "Пароль должен содержать минимум 8 символов, включая буквы и цифры");
            req.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(req, resp);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Пароли не совпадают");
            req.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(req, resp);
            return;
        }
        
        try {
            User user = new User();
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setFirstName(firstName != null ? firstName.trim() : null);
            user.setLastName(lastName != null ? lastName.trim() : null);
            
            UserDto registeredUser = userService.register(user, password);
            
            categoryService.createDefaultCategories(registeredUser.getId());
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
            
        } catch (UserAlreadyExistsException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(req, resp);
        }
    }
}