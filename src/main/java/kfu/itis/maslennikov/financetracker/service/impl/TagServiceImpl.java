package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.TagDao;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;
import kfu.itis.maslennikov.financetracker.service.TagService;
import kfu.itis.maslennikov.financetracker.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class TagServiceImpl implements TagService {
    
    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagDao.findById(id);
    }

    @Override
    public List<Tag> findByUserId(Long userId) {
        return tagDao.findByUserId(userId);
    }

    @Override
    public Long create(Tag tag) {
        ValidationUtil.validateTag(tag);
        return tagDao.create(tag);
    }

    @Override
    public boolean update(Tag tag, Long userId) {
        Optional<Tag> tagOpt = tagDao.findById(tag.getId());
        if (tagOpt.isEmpty()) {
            throw new ResourceNotFoundException("Тег не найден с таким id: " + tag.getId());
        }
        if (!tagOpt.get().getUserId().equals(userId)) {
            throw new ValidationException("У вас нет прав на изменение этого тега");
        }
        ValidationUtil.validateTag(tag);
        return tagDao.update(tag);
    }

    @Override
    public boolean delete(Long id, Long userId) {
        Optional<Tag> tagOpt = tagDao.findById(id);
        if (tagOpt.isEmpty()) {
            throw new ResourceNotFoundException("Тег не найден с таким id: " + id);
        }
        if (!tagOpt.get().getUserId().equals(userId)) {
            throw new ValidationException("У вас нет прав на изменение этого тега");
        }
        return tagDao.delete(id);
    }
}