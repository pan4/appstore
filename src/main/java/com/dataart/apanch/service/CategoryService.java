package com.dataart.apanch.service;

import com.dataart.apanch.model.Category;
import com.dataart.apanch.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> findAll(){
        List<Category> categories = new ArrayList();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    public Category findOne(Integer id){
        return categoryRepository.findOne(id);
    }

}