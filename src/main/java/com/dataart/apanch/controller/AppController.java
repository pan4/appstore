package com.dataart.apanch.controller;

import com.dataart.apanch.model.App;
import com.dataart.apanch.model.Category;
import com.dataart.apanch.model.CategoryType;
import com.dataart.apanch.service.AppPackageService;
import com.dataart.apanch.service.AppService;
import com.dataart.apanch.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    AppService appService;

    @Autowired
    AppPackageService appPackageService;

    private static Integer RECORDS_PER_PAGE = 2;

    @RequestMapping(value = {"/category-{id}"}, method = RequestMethod.GET)
    public String listCategories(@PathVariable int id, @RequestParam("page") int page, ModelMap model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", id);
        Page<App> apps = appService.findByCategoryId(id, new PageRequest(page - 1, RECORDS_PER_PAGE));
        model.addAttribute("apps", apps.getContent());
        model.addAttribute("noOfPages", apps.getTotalPages());
        return "home";
    }

    @RequestMapping(value = {"/new"}, method = RequestMethod.GET)
    public String newApp(ModelMap model) {
        App app = new App();
        model.addAttribute("app", app);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("selected", CategoryType.EDUCATION.getCategoryType());
        return "new";
    }

    @RequestMapping(value = {"/new"}, method = RequestMethod.POST)
    public String saveApp(MultipartFile file, App app, ModelMap model) throws IOException {
        appService.save(file, app);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", 1);
        Page<App> apps = appService.findByCategoryId(1,  new PageRequest(0, 2));
        model.addAttribute("apps", apps.getContent());
        model.addAttribute("noOfPages", apps.getTotalPages());
        return "home";
    }

    @RequestMapping(value = {"/app-{id}"}, method = RequestMethod.GET)
    public String getApp(@PathVariable int id, ModelMap model) {
        App app = appService.findById(id);
        model.addAttribute("app", app);
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

//    @RequestMapping(value = {"/app-{id}"}, method = RequestMethod.GET)
//    public String getApp(@PathVariable Integer id, ModelMap model) {
//        List<Category> categories = categoryService.findAll();
//        model.addAttribute("categories", categories);
//        List<App> apps = appService.findByCategoryId(id);
//        model.addAttribute("apps", apps);
//        return "home";
//    }


//    @RequestMapping(value = { "/category-{id}" }, method = RequestMethod.GET)
//    public String getCategory(@PathVariable Integer id, ModelMap model){
//        Category one = categoryService.findOne(id);
//        return "index";
//    }

}
