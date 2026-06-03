package io.github.matheushenriquereiter.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "The name must not be empty")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters long")
    private String name;

    @NotBlank(message = "The email must not be empty")
    @Email(message = "The email must be valid.")
    private String email;
}
