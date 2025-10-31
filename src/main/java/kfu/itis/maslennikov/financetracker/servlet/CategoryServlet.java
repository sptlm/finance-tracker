package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.Account;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        this.categoryService = (CategoryService) getServletContext().getAttribute("categoryService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();
        
        List<Category> categories = categoryService.findByUserId(userId);
        
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/categories.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                handleCreate(req, user.getId());
                req.getSession().setAttribute("successMessage", "Категория успешно создана");
            } else if ("update".equals(action)) {
                handleUpdate(req);
                req.getSession().setAttribute("successMessage", "Категория успешно обновлена");
            } else if ("delete".equals(action)) {
                handleDelete(req);
                req.getSession().setAttribute("successMessage", "Категория успешно удалена");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/categories");
    }

    private void handleCreate(HttpServletRequest req, Long userId) {
        String name = req.getParameter("name");
        String type = req.getParameter("type");
        String color = req.getParameter("color");
        String icon = req.getParameter("icon");

        Category category = new Category(null, userId, name, type, color, icon);
        categoryService.create(category);
    }
    
    private void handleUpdate(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String type = req.getParameter("type");
        String color = req.getParameter("color");
        String icon = req.getParameter("icon");
        
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Категория не найдена с таким id: " + id);
        }
        category.get().setName(name);
        category.get().setType(type);
        category.get().setColor(color);
        category.get().setIcon(icon);
        
        categoryService.update(category.orElse(null));
    }
    
    private void handleDelete(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();
        categoryService.delete(id,userId);
    }
}