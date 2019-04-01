package com.dataart.apanch.converter;

import com.dataart.apanch.model.Category;
import com.dataart.apanch.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryIdToCategoryConverter implements Converter<Object, Category> {

    static final private Logger logger = LoggerFactory.getLogger(CategoryIdToCategoryConverter.class);

    @Autowired
    CategoryService categoryService;

    public Category convert(Object element) {
        Integer id = Integer.parseInt((String) element);
        Category category = categoryService.findById(id);
        logger.info("Profile : {}", category);
        return category;
    }

}