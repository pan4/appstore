package com.dataart.apanch.converter;

import com.dataart.apanch.model.CategoryType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryTypeConverter implements Converter<String, CategoryType> {
    @Override
    public CategoryType convert(String source) {
        try {
            return CategoryType.valueOf(source.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}