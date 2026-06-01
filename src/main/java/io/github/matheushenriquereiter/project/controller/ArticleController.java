package io.github.matheushenriquereiter.project.controller;

import io.github.matheushenriquereiter.project.dto.ArticleDTO;
import io.github.matheushenriquereiter.project.dto.UserDTO;
import io.github.matheushenriquereiter.project.model.Article;
import io.github.matheushenriquereiter.project.model.ArticleForm;
import io.github.matheushenriquereiter.project.model.User;
import io.github.matheushenriquereiter.project.service.ArticleService;
import io.github.matheushenriquereiter.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ArticleController {
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/add-article")
    public String addArticle(Model model) {
        ArticleForm articleForm = new ArticleForm();
        model.addAttribute("articleForm", articleForm);

        return "add-article";
    }

    @PostMapping("/add-article")
    public String processAddArticle(@Valid @ModelAttribute("articleForm") ArticleForm articleForm, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-article";
        }

        String loggedUserEmail = principal.getName();
        articleService.saveAndLinkToUser(articleForm, loggedUserEmail);

        redirectAttributes.addFlashAttribute("articleForm", articleForm);

        return "redirect:/add-article-success";
    }

    @GetMapping("/add-article-success")
    public String addArticleSuccess(@Valid @ModelAttribute("articleForm") ArticleForm articleForm) {
        return "add-article-success";
    }

    @GetMapping("/list-articles")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "list-articles";
    }
}
