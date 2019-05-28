package com.dataart.apanch.service;

import com.dataart.apanch.model.App;
import com.dataart.apanch.model.AppPackage;
import com.dataart.apanch.model.CategoryType;
import com.dataart.apanch.repository.AppRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import static com.dataart.apanch.model.FileElements.APP_NAME;
import static com.dataart.apanch.model.FileElements.BIG_ICON_NAME;
import static com.dataart.apanch.model.FileElements.PACKAGE_NAME;
import static com.dataart.apanch.model.FileElements.SMALL_ICON_NAME;

@Slf4j
@Service
@Transactional
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
        Page<App> page = appRepository.findAll(PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "downloadsCount")));
        return page.getContent();
    }

    @Override
    public boolean trySave(MultipartFile file, App app, BindingResult result) throws IOException {
        Map<String, String> info = extractAppInfo(file);

        checkIfAppNameValid(app, info, result);
        checkIfAppNotExist(app, result);
        if (result.hasErrors()) {
            return false;
        }
        String packageName = StringUtils.isEmpty(info.get(PACKAGE_NAME.getTitle())) ?
                app.getName() + ".zip" : info.get(PACKAGE_NAME.getTitle());
        app.setPackageName(packageName);
        app.setDownloadsCount(0);

        AppPackage appPackage = buildAppPackage(file, info);
        app.setAppPackage(appPackage);

        appRepository.save(app);
        CategoryType type = app.getCategory().getType();
        log.info("New app with name = {} was added to {} categroy", app.getName(), type.name());
        return true;
    }

    private boolean isAppUnique(App app) {
        Optional<App> result = appRepository.findByNameAndCategoryType(app.getName(), app.getCategory().getType());
        return !result.isPresent();
    }

    private void checkIfAppNotExist(App app, BindingResult result) {
        if (!isAppUnique(app)) {
            CategoryType type = app.getCategory().getType();
            String[] args = {app.getName(), type.name()};
            FieldError ssoError = new FieldError("app", "name",
                    messageSource.getMessage("Non.unique.app", args, Locale.getDefault()));
            result.addError(ssoError);
            log.warn("App with name = {} within {} categroy already exists", app.getName(), type.name());
        }
    }

    private void checkIfAppNameValid(App app, Map<String, String> descriptorInfo, BindingResult result) {
        if (StringUtils.isEmpty(descriptorInfo.get(APP_NAME.getTitle())) ||
                !descriptorInfo.get(APP_NAME.getTitle()).equals(app.getName())) {
            result.addError(new FieldError("app", "name",
                    messageSource.getMessage("NotEqual.app.name", null, Locale.getDefault())));
            CategoryType type = app.getCategory().getType();
            log.warn("App name in not valid", app.getName(), type.name());
        }
    }

    private static AppPackage buildAppPackage(MultipartFile file, Map<String, String> descriptorInfo) throws IOException {
        AppPackage appPackage = new AppPackage();

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                byte[] buffer = streamToArray(zis);
                if (isEntryAppDescriptor(zipEntry)) {
                    appPackage.setFile(buffer);
                    appPackage.setFileName(zipEntry.getName());
                } else if (isEntrySmallIcon(descriptorInfo, zipEntry)) {
                    appPackage.setSmallIcon(buffer);
                    appPackage.setSmallIconName(zipEntry.getName());
                } else if (isEntryBigIcon(descriptorInfo, zipEntry)) {
                    appPackage.setBigIcon(buffer);
                    appPackage.setBigIconName(zipEntry.getName());
                }
                zipEntry = zis.getNextEntry();
            }
        }
        return appPackage;
    }

    private static boolean isEntrySmallIcon(Map<String, String> descriptorInfo, ZipEntry zipEntry) {
        return zipEntry.getName().equals(descriptorInfo.get(SMALL_ICON_NAME.getTitle()));
    }

    private static boolean isEntryBigIcon(Map<String, String> descriptorInfo, ZipEntry zipEntry) {
        return zipEntry.getName().equals(descriptorInfo.get(BIG_ICON_NAME.getTitle()));
    }

    private static Map<String, String> extractAppInfo(MultipartFile file) throws IOException {
        Map<String, String> info = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream());
             Scanner scanner = new Scanner(zis)) {

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (isEntryAppDescriptor(zipEntry)) {
                    parseAppDescriptor(info, scanner);
                    break;
                }
                zipEntry = zis.getNextEntry();
            }
        }
        return info;
    }

    private static void parseAppDescriptor(Map<String, String> info, Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String key = line.substring(0, line.indexOf(':')).trim();
            String value = line.substring(line.indexOf(':') + 1).trim();
            info.put(key, value);
        }
    }

    private static boolean isEntryAppDescriptor(ZipEntry zipEntry) {
        String entryName = zipEntry.getName();
        return entryName.toLowerCase().endsWith(".txt");
    }

    private static byte[] streamToArray(InputStream stream) throws IOException {
        byte[] result;
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            result = buffer.toByteArray();
        }
        return result;
    }
}
