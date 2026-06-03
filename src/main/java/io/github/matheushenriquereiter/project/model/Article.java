package io.github.matheushenriquereiter.project.model;

import io.github.matheushenriquereiter.project.dto.ArticleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_id_seq", allocationSize = 1)
    private Long id;

    private String title;
    private String resume;

    @ManyToMany(mappedBy = "articles")
    private Set<User> users = new HashSet<>();

    public Article(String title, String resume) {
        this.title = title;
        this.resume = resume;
    }

    public ArticleDTO toDTO() {
        return new ArticleDTO(this.getTitle(), this.getResume());
    }
}
