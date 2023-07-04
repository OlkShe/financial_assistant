package org.financial.assistant.controllers;

import org.financial.assistant.models.Category;
import org.financial.assistant.models.User;
import org.financial.assistant.security.UserDetails;
import org.financial.assistant.services.CategoryService;
import org.financial.assistant.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserDetailsService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserDetailsService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @GetMapping
    public String index(Model model,
                        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userDetails.getUser();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Categories - Financial assistant");
        model.addAttribute("bodyFragment", "category/index");
        model.addAttribute("categories", categoryService.getAllCategoriesForUser(user));

        return "wrapper";
    }

    @PostMapping
    public String createCategory(
            @ModelAttribute("category") Category category,
            @RequestParam("name") String categoryName,
            @RequestParam("type") String categoryType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userDetails.getUser();

        category.setName(categoryName);
        category.setType(categoryType);
        category.setUser(user);

        categoryService.createCategory(category);

        return "redirect:/category";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") Long categoryId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetails.getUser();
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        categoryService.deleteCategory(categoryId, user.getId());

//        return ResponseEntity.ok("Category deleted");
        return "redirect:/category";
    }
}
