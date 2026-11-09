package io.github.matheushenriquereiter.project.controller;

import io.github.matheushenriquereiter.project.dto.ItemDTO;
import io.github.matheushenriquereiter.project.dto.LabIdDTO;
import io.github.matheushenriquereiter.project.exceptions.BusinessException;
import io.github.matheushenriquereiter.project.model.Item;
import io.github.matheushenriquereiter.project.model.ItemForm;
import io.github.matheushenriquereiter.project.model.Lab;
import io.github.matheushenriquereiter.project.repository.ItemRepository;
import io.github.matheushenriquereiter.project.repository.LabRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        List<ItemDTO> labItemDTOs = labItems.stream().map(Item::toDTO).toList();

        return ResponseEntity.ok(labItemDTOs);
    }

    @GetMapping("/{id}")
    public String showLabItems(@PathVariable Integer id, Model model) {
        Lab lab = labRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));

        List<Item> labItems = itemRepository.findAllByLab(lab);

        List<ItemDTO> labItemDTOs = labItems.stream().map(Item::toDTO).toList();

        model.addAttribute("lab", lab.toDTO());
        model.addAttribute("labItems", labItemDTOs);

        return "list-items";
    }

    @GetMapping("/{id}/add")
    public String addItem(@PathVariable Integer id, Model model) {
        labRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));

        ItemForm itemForm = new ItemForm();
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("labId", id);

        return "add-item";
    }

    @PostMapping("/{id}/add")
    public String processAddItem(@PathVariable Integer id, @Valid @ModelAttribute("itemForm") ItemForm itemForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("labId", id);
            return "add-item";
        }

        Lab lab = labRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));

        Item item = new Item(itemForm.getName(), itemForm.getLocation(), itemForm.getDescription());
        item.setLab(lab);
        itemRepository.save(item);

        return "redirect:/items/" + id;
    }

    @GetMapping("/{labId}/update/{itemId}")
    public String updateItem(@PathVariable Integer labId, @PathVariable Integer itemId, Model model) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Item not found"));

        ItemForm itemForm = new ItemForm(item.getName(), item.getLocation(), item.getDescription());
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("labId", labId);
        model.addAttribute("itemId", itemId);

        return "update-item";
    }

    @PostMapping("/{labId}/update/{itemId}")
    public String processUpdateItem(@PathVariable Integer labId, @PathVariable Integer itemId, @Valid @ModelAttribute("itemForm") ItemForm itemForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("labId", labId);
            model.addAttribute("itemId", itemId);
            return "update-item";
        }

        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Item not found"));

        item.setName(itemForm.getName());
        item.setLocation(itemForm.getLocation());
        item.setDescription(itemForm.getDescription());
        itemRepository.save(item);

        return "redirect:/items/" + labId;
    }

    @GetMapping("/{labId}/delete/{itemId}")
    public String deleteItem(@PathVariable Integer labId, @PathVariable Integer itemId) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Item not found"));

        itemRepository.delete(item);

        return "redirect:/items/" + labId;
    }
}
