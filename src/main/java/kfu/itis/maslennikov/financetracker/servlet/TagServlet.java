package kfu.itis.maslennikov.financetracker.servlet;

import kfu.itis.maslennikov.financetracker.dto.UserDto;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.service.TagService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/tags")
public class TagServlet extends HttpServlet {
    
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        this.tagService = (TagService) getServletContext().getAttribute("tagService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();
        
        List<Tag> tags = tagService.findByUserId(userId);
        
        req.setAttribute("tags", tags);
        req.getRequestDispatcher("/WEB-INF/views/tags.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                handleCreate(req, user.getId());
                req.getSession().setAttribute("successMessage", "Тег успешно создан");
            } else if ("update".equals(action)) {
                handleUpdate(req);
                req.getSession().setAttribute("successMessage", "Тег успешно обновлён");
            } else if ("delete".equals(action)) {
                handleDelete(req);
                req.getSession().setAttribute("successMessage", "Тег успешно удалён");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/tags");
    }
    
    private void handleCreate(HttpServletRequest req, Long userId) {
        String name = req.getParameter("name");
        String color = req.getParameter("color");

        Tag tag = new Tag(null, userId, name, color);
        
        tagService.create(tag);
    }
    
    private void handleUpdate(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String color = req.getParameter("color");
        
        Optional<Tag> tag = tagService.findById(id);
        if (tag.isEmpty()) {
            throw new ResourceNotFoundException("Аккаунт не найден с таким id: " + id);
        }
        tag.get().setName(name);
        tag.get().setColor(color);
        tagService.update(tag.orElse(null));
    }
    
    private void handleDelete(HttpServletRequest req) {
        Long id = Long.parseLong(req.getParameter("id"));
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long userId = user.getId();
        tagService.delete(id, userId);
    }
}