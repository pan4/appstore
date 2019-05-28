package com.dataart.apanch.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileNotEmptyValidator implements ConstraintValidator<FileNotEmpty, Object> {
    @Override
    public void initialize(FileNotEmpty fileNotEmpty) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        MultipartFile multipartFile = (MultipartFile) o;
        return multipartFile.getSize() != 0;
    }
}
