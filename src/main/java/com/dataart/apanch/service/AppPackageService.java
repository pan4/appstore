package com.dataart.apanch.service;

import com.dataart.apanch.model.AppPackage;
import com.dataart.apanch.repository.AppPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class AppPackageService {
    @Autowired
    AppPackageRepository appPackageRepository;

    public void save(AppPackage appPackage){
        appPackageRepository.save(appPackage);
    }

    public AppPackage findByAppId(Integer id){
        return appPackageRepository.findByAppId(id);
    }

    @Transactional
    public void downloadPackage(Integer id, ServletOutputStream servletOutputStream) {
        try {
            AppPackage appPackage = findByAppId(id);
            ZipOutputStream zipOut = new ZipOutputStream(servletOutputStream);
            Map<String, byte[]> map = new HashMap<>();
            map.put(appPackage.getFileName(), appPackage.getFile());
            map.put(appPackage.getSmallIconName(), appPackage.getSmallIcon());
            map.put(appPackage.getBigIconName(), appPackage.getBigIcon());
            for (String key : map.keySet()) {
                InputStream is = new ByteArrayInputStream(map.get(key));
                ZipEntry zipEntry = new ZipEntry(key);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(map.get(key));
                is.close();
            }
            zipOut.close();
            appPackage.getApp().increaseDownloadsCount();
            save(appPackage);
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
