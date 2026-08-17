package io.github.matheushenriquereiter.project.model;

import io.github.matheushenriquereiter.project.dto.ArticleDTO;
import io.github.matheushenriquereiter.project.dto.LabDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lab")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lab_seq")
    @SequenceGenerator(name = "lab_seq", sequenceName = "lab_id_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String location;
    private String subject;

    @ManyToMany(mappedBy = "labs")
    private Set<User> users = new HashSet<>();

    public Lab(String name, String location, String subject) {
        this.name = name;
        this.location = location;
        this.subject = subject;
    }

    public LabDTO toDTO() {
        return new LabDTO(this.getName(), this.getLocation(), this.getSubject());
    }



}
