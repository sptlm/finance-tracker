package kfu.itis.maslennikov.financetracker.service.impl;

import kfu.itis.maslennikov.financetracker.dao.TagDao;
import kfu.itis.maslennikov.financetracker.entity.Tag;
import kfu.itis.maslennikov.financetracker.exception.ResourceNotFoundException;
import kfu.itis.maslennikov.financetracker.exception.ValidationException;
import kfu.itis.maslennikov.financetracker.service.TagService;

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
        if (tag.getName() == null || tag.getName().trim().isEmpty()) {
            throw new ValidationException("Tag name cannot be empty");
        }
        
        return tagDao.create(tag);
    }

    @Override
    public boolean update(Tag tag) {
        if (tagDao.findById(tag.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Tag not found with id: " + tag.getId());
        }
        
        return tagDao.update(tag);
    }

    @Override
    public boolean delete(Long id, Long userId) {
        Optional<Tag> tagOpt = tagDao.findById(id);
        
        if (tagOpt.isEmpty()) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        
        if (!tagOpt.get().getUserId().equals(userId)) {
            throw new ValidationException("You don't have permission to delete this tag");
        }
        
        return tagDao.delete(id);
    }
}