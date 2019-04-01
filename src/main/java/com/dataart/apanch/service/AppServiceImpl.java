package com.dataart.apanch.service;

import com.dataart.apanch.model.App;
import com.dataart.apanch.model.AppPackage;
import com.dataart.apanch.model.CategoryType;
import com.dataart.apanch.model.FileElements;
import com.dataart.apanch.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    AppRepository appRepository;

    @Autowired
    MessageSource messageSource;

    @Override
    public Page<App> findByCategoryType(CategoryType type, Pageable pageable) {
        return appRepository.findByCategoryType(type.name(), pageable);
    }

    @Override
    public App findById(Integer id) {
        return appRepository.findById(id).get();
    }

    @Override
    public List<App> findPopular() {
        Page<App> page = appRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "downloadsCount")));
        return page.getContent();
    }

    @Override
    public boolean trySave(MultipartFile file, App app, BindingResult result) throws IOException {
        AppPackage appPackage = parse(file, app, result);
        if (result.hasErrors()) {
            return false;
        }
        app.setDownloadsCount(0);
        app.setAppPackage(appPackage);
        appRepository.save(app);
        return true;
    }

    @Override
    public boolean isAppUnique(App app) {
        App result = appRepository.findByNameAndCategoryType(app.getName(), app.getCategory().getType());
        return result == null;
    }

    private AppPackage parse(MultipartFile file, App app, BindingResult result) throws IOException {
        AppPackage appPackage = new AppPackage();
        Map<String, String> info = new HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream()); Scanner scanner = new Scanner(zis)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (zipEntry.getName().toLowerCase().endsWith(".txt")) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        info.put(line.substring(0, line.indexOf(':')).trim(), line.substring(line.indexOf(':') + 1).trim());
                    }
                    break;
                }
                zipEntry = zis.getNextEntry();
            }
        }
        if (!info.get(FileElements.APP_NAME.getTitle()).equals(app.getName())) {
            result.addError(new FieldError("app", "name", messageSource.getMessage("NotEqual.app.name", null, Locale.getDefault())));
        }

        String packageName = StringUtils.isEmpty(info.get(FileElements.PACKAGE_NAME.getTitle())) ? app.getName() + ".zip" : info.get(FileElements.PACKAGE_NAME.getTitle());
        app.setPackageName(packageName);

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                byte[] buffer = streamToArray(zis);
                if (zipEntry.getName().toLowerCase().endsWith(".txt")) {
                    appPackage.setFile(buffer);
                    appPackage.setFileName(zipEntry.getName());
                } else if (zipEntry.getName().equals(info.get(FileElements.SMALL_ICON_NAME.getTitle()))) {
                    appPackage.setSmallIcon(buffer);
                    appPackage.setSmallIconName(zipEntry.getName());
                } else if (zipEntry.getName().equals(info.get(FileElements.BIG_ICON_NAME.getTitle()))) {
                    appPackage.setBigIcon(buffer);
                    appPackage.setBigIconName(zipEntry.getName());
                }
                zipEntry = zis.getNextEntry();
            }
        }

        return appPackage;
    }

    private static byte[] streamToArray(InputStream strem) throws IOException {
        byte[] result;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = strem.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            result = buffer.toByteArray();
        }
        return result;
    }
}
