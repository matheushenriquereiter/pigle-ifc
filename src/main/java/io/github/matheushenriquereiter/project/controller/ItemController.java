package io.github.matheushenriquereiter.project.controller;

import io.github.matheushenriquereiter.project.dto.ItemDTO;
import io.github.matheushenriquereiter.project.dto.LabIdDTO;
import io.github.matheushenriquereiter.project.exceptions.BusinessException;
import io.github.matheushenriquereiter.project.model.Item;
import io.github.matheushenriquereiter.project.model.Lab;
import io.github.matheushenriquereiter.project.repository.ItemRepository;
import io.github.matheushenriquereiter.project.repository.LabRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;
    private final LabRepository labRepository;

    public ItemController(ItemRepository itemRepository, LabRepository labRepository) {
        this.itemRepository = itemRepository;
        this.labRepository = labRepository;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getLabItems(@Valid @RequestBody LabIdDTO labIdDTO, Principal principal) {
        Lab lab = labRepository
                .findById(labIdDTO.id())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));

        List<Item> labItems = itemRepository.findAllByLab(lab);

        List<ItemDTO> labItemDTOs = labItems.stream().map(labItem -> new ItemDTO(labItem.getName(), labItem.getLocation(), labItem.getDescription())).toList();

        return ResponseEntity.ok(labItemDTOs);
    }
}
