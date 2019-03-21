package com.dataart.apanch.controller;

import com.dataart.apanch.model.App;
import com.dataart.apanch.model.Category;
import com.dataart.apanch.model.CategoryType;
import com.dataart.apanch.service.AppService;
import com.dataart.apanch.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    AppService appService;

    @RequestMapping(value = {"/category-{id}"}, method = RequestMethod.GET)
    public String listCategories(@PathVariable Integer id, ModelMap model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        List<App> apps = appService.findByCategoryId(id);
        model.addAttribute("apps", apps);
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
    public String saveApp(App app, BindingResult result, ModelMap model) {
        app.setPackageName("package.zip");
        appService.save(app);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        List<App> apps = appService.findByCategoryId(1);
        model.addAttribute("apps", apps);

        return "home";
    }


//    @RequestMapping(value = { "/category-{id}" }, method = RequestMethod.GET)
//    public String getCategory(@PathVariable Integer id, ModelMap model){
//        Category one = categoryService.findOne(id);
//        return "index";
//    }

}
