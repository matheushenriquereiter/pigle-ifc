package io.github.matheushenriquereiter.project.controller;

import io.github.matheushenriquereiter.project.dto.LabDTO;
import io.github.matheushenriquereiter.project.model.LabForm;
import io.github.matheushenriquereiter.project.service.LabService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class LabController {
    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping("/add-lab")
    public String addLab(Model model) {
        LabForm labForm = new LabForm();
        model.addAttribute("labForm", labForm);

        return "add-lab";
    }

    @PostMapping("/add-lab")
    public String processAddArticle(@Valid @ModelAttribute("labForm") LabForm labForm, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-lab";
        }

        String loggedUserEmail = principal.getName();
        labService.createAndLinkToUser(labForm, loggedUserEmail);

        redirectAttributes.addFlashAttribute("labForm", labForm);

        return "redirect:/add-lab-success";
    }

    @GetMapping("/add-lab-success")
    public String addLabSuccess(@Valid @ModelAttribute("labForm") LabForm labForm) {
        return "add-lab-success";
    }

    @GetMapping("/home")
    public String listUserLabs(Model model, Principal principal) {
        String loggedUserEmail = principal.getName();
        List<LabDTO> userLabs = labService.findAllByUserEmail(loggedUserEmail);

        model.addAttribute("userLabs", userLabs);

        return "home";
    }

    @GetMapping("/update-lab/{id}")
    public String updateLab(@PathVariable Integer id, Model model) {
        LabDTO lab = labService.findById(id);
        LabForm labForm = new LabForm(lab.getName(), lab.getLocation(), lab.getSubject());

        model.addAttribute("labForm", labForm);
        model.addAttribute("labId", id);

        return "update-lab";
    }

    @PostMapping("/update-lab/{id}")
    public String processUpdateLab(@PathVariable Integer id, @Valid @ModelAttribute("labForm") LabForm labForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("labId", id);
            return "update-lab";
        }

        labService.updateLab(id, labForm);

        return "redirect:/home";
    }

    @GetMapping("/delete-lab/{id}")
    public String deleteLab(@PathVariable Integer id) {
        labService.deleteById(id);

        return "redirect:/home";
    }
}
