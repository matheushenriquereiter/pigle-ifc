package io.github.matheushenriquereiter.project.repository;

import io.github.matheushenriquereiter.project.model.Item;
import io.github.matheushenriquereiter.project.model.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByLab(Lab lab);
}
