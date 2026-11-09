package io.github.matheushenriquereiter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LabDTO {
    private Long id;
    private String name;
    private String location;
    private String subject;

    public LabDTO(String name, String location, String subject) {
        this.name = name;
        this.location = location;
        this.subject = subject;
    }
}
