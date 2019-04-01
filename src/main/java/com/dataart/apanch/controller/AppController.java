package com.dataart.apanch.controller;

import com.dataart.apanch.model.App;
import com.dataart.apanch.model.CategoryType;
import com.dataart.apanch.service.AppPackageService;
import com.dataart.apanch.service.AppService;
import com.dataart.apanch.service.CategoryService;
import com.dataart.apanch.service.DefaultIconsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    AppService appService;

    @Autowired
    AppPackageService appPackageService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    DefaultIconsService defaultIconsService;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        return "redirect:/category-games";
    }

    @RequestMapping(value = {"/category-{type}"}, method = RequestMethod.GET)
    public String listCategories(@PathVariable CategoryType type, ModelMap model, @RequestParam(value = "orderBy", defaultValue = "downloadsCount") String orderBy,
                                 @RequestParam(value = "direction", defaultValue = "desc") String direction, @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "2") int size) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoryType", type.name().toLowerCase());
        Page<App> apps = appService.findByCategoryType(type, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(direction), orderBy)));
        model.addAttribute("apps", apps.getContent());
        model.addAttribute("noOfPages", apps.getTotalPages());
        model.addAttribute("popular", appService.findPopular());
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("defaultIcons", defaultIconsService.get());
        return "home";
    }

    @RequestMapping(value = {"/new"}, method = RequestMethod.GET)
    public String newApp(ModelMap model) {
        model.addAttribute("app", new App());
        fillModel(model);
        return "new";
    }

    @RequestMapping(value = {"/new"}, method = RequestMethod.POST)
    public String saveApp(MultipartFile file, @Valid App app, BindingResult result, ModelMap model) throws IOException {
        if (result.hasErrors() || file.getSize() == 0) {
            fillModel(model);
            if (file.getSize() == 0) {
                FieldError error = new FieldError("app", "appPackage", messageSource.getMessage("NotEmpty.app.appPackage", null, Locale.getDefault()));
                result.addError(error);
            }
            return "new";
        }
        if (!appService.isAppUnique(app)) {
            fillModel(model);
            FieldError ssoError = new FieldError("app", "name", messageSource.getMessage("Non.unique.app", new String[]{app.getName(), app.getCategory().getType()}, Locale.getDefault()));
            result.addError(ssoError);
            return "new";
        }
        appService.trySave(file, app, result);
        if (result.hasErrors()) {
            fillModel(model);
            return "new";
        }
        return "redirect:/category-games";
    }

    private void fillModel(ModelMap model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("popular", appService.findPopular());
        model.addAttribute("loggedinuser", getPrincipal());
        model.addAttribute("defaultIcons", defaultIconsService.get());
    }

    @RequestMapping(value = {"/app-{id}"}, method = RequestMethod.GET)
    public String getApp(@PathVariable int id, ModelMap model) {
        model.addAttribute("app", appService.findById(id));
        model.addAttribute("popular", appService.findPopular());
        model.addAttribute("defaultIcons", defaultIconsService.get());
        return "details";
    }

    @RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
    public void downloadPackage(@PathVariable String fileName, @RequestParam("id") int id, HttpServletResponse response) {
        try {
            appPackageService.downloadPackage(id, response.getOutputStream());
            response.setContentType("application/zip");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        } catch (IOException e) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
    }

    private static String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
