package com.dataart.apanch.dto;

import com.dataart.apanch.validation.FileNotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ValidatedFile {
    @FileNotEmpty
    MultipartFile file;
}
