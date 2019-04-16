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
import java.util.Optional;
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
        return appRepository.findByCategoryType(type, pageable);
    }

    @Override
    public Optional<App> findById(Integer id) {
        return appRepository.findById(id);
    }

    @Override
    public List<App> findPopular() {
        Page<App> page = appRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "downloadsCount")));
        return page.getContent();
    }

    @Override
    public boolean trySave(MultipartFile file, App app, BindingResult result) throws IOException {
        Map<String, String> info = parseDescriptor(file);
        validate(file, app, info, result);
        if (result.hasErrors()) {
            return false;
        }
        String packageName = StringUtils.isEmpty(info.get(FileElements.PACKAGE_NAME.getTitle())) ?
                app.getName() + ".zip" : info.get(FileElements.PACKAGE_NAME.getTitle());
        app.setPackageName(packageName);
        app.setDownloadsCount(0);

        AppPackage appPackage = buildAppPackage(file, info);
        app.setAppPackage(appPackage);

        appRepository.save(app);
        return true;
    }

    @Override
    public boolean isAppUnique(App app) {
        Optional<App> result = appRepository.findByNameAndCategoryType(app.getName(), app.getCategory().getType());
        return !result.isPresent();
    }

    // TODO: what will you do in case if you need to add 2 more validation rules? 2 more if block? in case of 10 more?? Should be refactored
    private void validate(MultipartFile file, App app, Map<String, String> descriptorInfo, BindingResult result) {
        if (file.getSize() == 0) {
            FieldError error = new FieldError("app", "appPackage",
                    messageSource.getMessage("NotEmpty.app.appPackage", null, Locale.getDefault()));
            result.addError(error);
        }
        if (StringUtils.isEmpty(descriptorInfo.get(FileElements.APP_NAME.getTitle())) ||
                !descriptorInfo.get(FileElements.APP_NAME.getTitle()).equals(app.getName())) {
            result.addError(new FieldError("app", "name",
                    messageSource.getMessage("NotEqual.app.name", null, Locale.getDefault())));
        }
        if (!isAppUnique(app)) {
            String[] args = {app.getName(), app.getCategory().getType().name()};
            FieldError ssoError = new FieldError("app", "name",
                    messageSource.getMessage("Non.unique.app", args, Locale.getDefault()));
            result.addError(ssoError);
        }
    }

    private static AppPackage buildAppPackage(MultipartFile file, Map<String, String> descriptorInfo) throws IOException {
        AppPackage appPackage = new AppPackage();

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                byte[] buffer = streamToArray(zis);
                if (zipEntry.getName().toLowerCase().endsWith(".txt")) {
                    appPackage.setFile(buffer);
                    appPackage.setFileName(zipEntry.getName());
                } else if (zipEntry.getName().equals(descriptorInfo.get(FileElements.SMALL_ICON_NAME.getTitle()))) {
                    appPackage.setSmallIcon(buffer);
                    appPackage.setSmallIconName(zipEntry.getName());
                } else if (zipEntry.getName().equals(descriptorInfo.get(FileElements.BIG_ICON_NAME.getTitle()))) {
                    appPackage.setBigIcon(buffer);
                    appPackage.setBigIconName(zipEntry.getName());
                }
                zipEntry = zis.getNextEntry();
            }
        }
        return appPackage;
    }

    // TODO: high level of complexity. Should be simplified
    private static Map<String, String> parseDescriptor(MultipartFile file) throws IOException {
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
        return info;
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
