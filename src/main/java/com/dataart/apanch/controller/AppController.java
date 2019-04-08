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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

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
        return "redirect:/category/games";
    }

    @RequestMapping(value = {"/category/{type}"}, method = RequestMethod.GET)
    public String listCategories(@PathVariable CategoryType type, ModelMap model,
                                 @RequestParam(value = "orderBy", defaultValue = "downloadsCount") String orderBy,
                                 @RequestParam(value = "direction", defaultValue = "desc") String direction,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "2") int size) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoryType", type.name().toLowerCase());
        Page<App> apps = appService.findByCategoryType(type, PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.fromString(direction), orderBy)));
        model.addAttribute("apps", apps.getContent());
        model.addAttribute("noOfPages", apps.getTotalPages());
        model.addAttribute("popular", appService.findPopular());
        model.addAttribute("loggedinuser", getUserName());
        model.addAttribute("defaultIcons", defaultIconsService.get().orElseThrow(() -> new EntityNotFoundException()));
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
        if (!appService.trySave(file, app, result)) {
            fillModel(model);
            return "new";
        }
        return "redirect:/category/games";
    }

    private void fillModel(ModelMap model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("popular", appService.findPopular());
        model.addAttribute("loggedinuser", getUserName());
        model.addAttribute("defaultIcons", defaultIconsService.get().orElseThrow(() -> new EntityNotFoundException()));
    }

    @RequestMapping(value = {"/app/{id}"}, method = RequestMethod.GET)
    public String getApp(@PathVariable int id, ModelMap model) {
        model.addAttribute("app", appService.findById(id).orElseThrow(() -> new EntityNotFoundException()));
        model.addAttribute("popular", appService.findPopular());
        model.addAttribute("defaultIcons", defaultIconsService.get().orElseThrow(() -> new EntityNotFoundException()));
        return "details";
    }

    @RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
    public void downloadPackage(@PathVariable String fileName,
                                @RequestParam("id") int id,
                                HttpServletResponse response) throws IOException {
        appPackageService.downloadPackage(id, response.getOutputStream());
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
    }

    @RequestMapping(value = "/access_denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getUserName());
        return "accessDenied";
    }

    private static String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
