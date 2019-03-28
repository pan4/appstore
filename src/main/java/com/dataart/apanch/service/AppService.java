package com.dataart.apanch.service;

import com.dataart.apanch.caching.CachingConfig;
import com.dataart.apanch.model.App;
import com.dataart.apanch.model.AppPackage;
import com.dataart.apanch.model.CategoryType;
import com.dataart.apanch.repository.AppPackageRepository;
import com.dataart.apanch.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class AppService {
    @Autowired
    AppRepository appRepository;

    @Autowired
    AppPackageRepository appPackageRepository;

    public Page<App> findByCategoryType(CategoryType type, Pageable pageable){
        return appRepository.findByCategoryType(type.name(), pageable);
    }

    public App findById(Integer id){
        return appRepository.findById(id);
    }

    @Cacheable(value = CachingConfig.POPULAR)
    public List<App> findPopular(){
        Page<App> page = appRepository.findAll(new PageRequest(0, 5, new Sort(Sort.Direction.DESC, "downloadsCount")));
        return page.getContent();
    }

    @Transactional
    public void save(MultipartFile file, App app) throws IOException {
        AppPackage appPackage = parse(file, app);
        app.setDownloadsCount(0);
        app.setAppPackage(appPackage);
        appRepository.save(app);
    }

    private AppPackage parse(MultipartFile file, App app) throws IOException {
        AppPackage appPackage = new AppPackage();
        Map<String, String> info = new HashMap<>();
        ZipInputStream zis = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            if(zipEntry.getName().toLowerCase().endsWith(".txt")){
                Scanner scanner = new Scanner(zis);
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    info.put(line.substring(0, line.indexOf(':')).trim(),line.substring(line.indexOf(':') + 1).trim());
                }
                break;
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        app.setPackageName(info.get("package"));

        zis = new ZipInputStream(file.getInputStream());
        zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            byte[] buffer = streamToArray(zis);
            if(zipEntry.getName().toLowerCase().endsWith(".txt")){
                appPackage.setFile(buffer);
                appPackage.setFileName(zipEntry.getName());
            } else if(zipEntry.getName().equals(info.get("picture_64"))){
                appPackage.setSmallIcon(buffer);
                appPackage.setSmallIconName(zipEntry.getName());
            } else if(zipEntry.getName().equals(info.get("picture_256"))){
                appPackage.setBigIcon(buffer);
                appPackage.setBigIconName(zipEntry.getName());
            }
            zipEntry = zis.getNextEntry();
        }

        return appPackage;
    }

    private byte[] streamToArray(InputStream strem) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = strem.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
