package io.github.matheushenriquereiter.project.model;

import io.github.matheushenriquereiter.project.dto.ItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@NoArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name = "item_seq", sequenceName = "item_id_seq", allocationSize = 1)
    private long id;

    private String name;
    private String location;
    private String description;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;

    public Item(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public ItemDTO toDTO() {
        return new ItemDTO(this.getName(), this.getLocation(), this.getDescription());
    }
}

