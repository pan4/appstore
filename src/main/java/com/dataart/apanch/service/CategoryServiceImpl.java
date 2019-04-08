package com.dataart.apanch.service;

import com.dataart.apanch.model.Category;
import com.dataart.apanch.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        // TODO: use JpaRepository instead of CRUD for using List
        List<Category> categories = new ArrayList();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    @Override
    // TODO: why don't just return Optional?
    public Category findById(Integer id) {
        return categoryRepository.findById(id).get();
    }

}
