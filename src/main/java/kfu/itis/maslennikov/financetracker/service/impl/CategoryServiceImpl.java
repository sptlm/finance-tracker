package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.CategoryDao;
import kfu.itis.maslennikov.financetracker.entity.Category;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;
import kfu.itis.maslennikov.financetracker.service.CategoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public List<Category> findByUserId(Long userId) {
        return categoryDao.findByUserId(userId);
    }

    @Override
    public List<Category> findByUserIdAndType(Long userId, String type) {
        return categoryDao.findByUserIdAndType(userId, type);
    }

    @Override
    public Long create(Category category) {
        // Валидация
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }
        
        if (!Arrays.asList("INCOME", "EXPENSE").contains(category.getType())) {
            throw new ValidationException("Category type must be INCOME or EXPENSE");
        }
        
        return categoryDao.create(category);
    }

    @Override
    public boolean update(Category category) {
        if (categoryDao.findById(category.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Category not found with id: " + category.getId());
        }
        
        return categoryDao.update(category);
    }

    @Override
    public boolean delete(Long id, Long userId) {
        Optional<Category> categoryOpt = categoryDao.findById(id);
        
        if (categoryOpt.isEmpty()) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        
        if (!categoryOpt.get().getUserId().equals(userId)) {
            throw new ValidationException("You don't have permission to delete this category");
        }
        
        return categoryDao.delete(id);
    }

    @Override
    public void createDefaultCategories(Long userId) {
        // Создание стандартных категорий расходов
        String[][] expenseCategories = {
            {"Продукты", "#FF6B6B", "food"},
            {"Транспорт", "#4ECDC4", "transport"},
            {"Здоровье", "#45B7D1", "health"},
            {"Развлечения", "#FFA07A", "entertainment"},
            {"Коммунальные услуги", "#98D8C8", "utilities"},
            {"Одежда", "#F7DC6F", "clothes"},
            {"Образование", "#BB8FCE", "education"},
            {"Прочее", "#85929E", "other"}
        };
        
        for (String[] cat : expenseCategories) {
            Category category = new Category();
            category.setUserId(userId);
            category.setName(cat[0]);
            category.setType("EXPENSE");
            category.setColor(cat[1]);
            category.setIcon(cat[2]);
            categoryDao.create(category);
        }
        
        // Создание стандартных категорий доходов
        String[][] incomeCategories = {
            {"Зарплата", "#52BE80", "salary"},
            {"Фриланс", "#5DADE2", "freelance"},
            {"Инвестиции", "#F8B739", "investments"},
            {"Подарки", "#EC7063", "gifts"},
            {"Прочее", "#85929E", "other"}
        };
        
        for (String[] cat : incomeCategories) {
            Category category = new Category();
            category.setUserId(userId);
            category.setName(cat[0]);
            category.setType("INCOME");
            category.setColor(cat[1]);
            category.setIcon(cat[2]);
            categoryDao.create(category);
        }
    }
}