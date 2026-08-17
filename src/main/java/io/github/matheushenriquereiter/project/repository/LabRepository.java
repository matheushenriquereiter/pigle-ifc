package io.github.matheushenriquereiter.project.repository;

import io.github.matheushenriquereiter.project.model.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab, Integer> {
    List<Lab> findAllByUsers_Email(String email);
}
