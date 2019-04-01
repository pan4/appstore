package com.dataart.apanch.service;

import com.dataart.apanch.model.AppPackage;

import javax.servlet.ServletOutputStream;

public interface AppPackageService {
    void save(AppPackage appPackage);

    AppPackage findByAppId(Integer id);

    void downloadPackage(Integer id, ServletOutputStream servletOutputStream);
}
