package io.github.matheushenriquereiter.project.controller;

import io.github.matheushenriquereiter.project.dto.ArticleDTO;
import io.github.matheushenriquereiter.project.model.ArticleForm;
import io.github.matheushenriquereiter.project.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
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
        articleService.createAndLinkToUser(articleForm, loggedUserEmail);

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

    @GetMapping("/list-user-articles")
    public String listUserArticles(Model model, Principal principal) {
        String loggedUserEmail = principal.getName();
        List<ArticleDTO> userArticles = articleService.findAllByUserEmail(loggedUserEmail);

        model.addAttribute("userArticles", userArticles);

        return "list-user-articles";
    }

    @GetMapping("/show-article/{title}")
    public String showArticle(Model model, @PathVariable String title) {
        ArticleDTO articleDTO = articleService.findByTitle(title);
        model.addAttribute("articleForm", articleDTO);

        return "show-article";
    }

}
