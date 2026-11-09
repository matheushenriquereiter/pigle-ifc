package io.github.matheushenriquereiter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDTO {
    private Long id;
    private String name;
    private String location;
    private String description;

    public ItemDTO(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }
}
