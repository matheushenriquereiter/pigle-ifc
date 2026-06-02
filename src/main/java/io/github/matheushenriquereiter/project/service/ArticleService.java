package io.github.matheushenriquereiter.project.service;

import io.github.matheushenriquereiter.project.dto.ArticleDTO;
import io.github.matheushenriquereiter.project.model.Article;
import io.github.matheushenriquereiter.project.model.ArticleForm;
import io.github.matheushenriquereiter.project.model.User;
import io.github.matheushenriquereiter.project.repository.ArticleRepository;
import io.github.matheushenriquereiter.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    ArticleRepository articleRepository;
    UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void saveAndLinkToUser(ArticleForm articleForm, String userEmail) {
        User loggedUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Article article = new Article();
        article.setTitle(articleForm.getTitle());
        article.setResume(articleForm.getResume());

        articleRepository.save(article);

        loggedUser.getArticles().add(article);
    }

    public List<ArticleDTO> findAll() {
        List<Article> articleList = articleRepository.findAll();

        return articleList.stream().map(Article::toDTO).toList();
    }
}
