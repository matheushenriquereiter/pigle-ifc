package io.github.matheushenriquereiter.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LabForm {
    private String name;
    private String location;
    private String subject;
}
