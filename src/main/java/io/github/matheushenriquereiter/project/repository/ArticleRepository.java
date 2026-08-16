package io.github.matheushenriquereiter.project.repository;

import io.github.matheushenriquereiter.project.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAllByUsers_Email(String email);

    List<Article> findByTitle(String title);
}
