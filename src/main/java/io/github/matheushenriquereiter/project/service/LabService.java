package io.github.matheushenriquereiter.project.service;

import io.github.matheushenriquereiter.project.dto.LabDTO;
import io.github.matheushenriquereiter.project.exceptions.BusinessException;
import io.github.matheushenriquereiter.project.model.*;
import io.github.matheushenriquereiter.project.repository.LabRepository;
import io.github.matheushenriquereiter.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LabService {
    LabRepository labRepository;
    UserRepository userRepository;

    public LabService(LabRepository labRepository, UserRepository userRepository) {
        this.labRepository = labRepository;
        this.userRepository = userRepository;
    }

    public void createAndLinkToUser(LabForm labForm, String userEmail) {
        User loggedUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        Lab lab = new Lab(labForm.getName(), labForm.getLocation(), labForm.getSubject());
        labRepository.save(lab);

        loggedUser.getLabs().add(lab);
    }

    public List<LabDTO> findAll() {
        List<Lab> labList = labRepository.findAll();

        return labList.stream().map(Lab::toDTO).toList();
    }

    public List<LabDTO> findAllByUserEmail(String email) {
        List<Lab> userLabs = labRepository.findAllByUsers_Email(email);

        return userLabs.stream().map(Lab::toDTO).toList();
    }

    public LabDTO findById(Integer id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));
        return lab.toDTO();
    }

    public void updateLab(Integer id, LabForm labForm) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));

        lab.setName(labForm.getName());
        lab.setLocation(labForm.getLocation());
        lab.setSubject(labForm.getSubject());

        labRepository.save(lab);
    }

    public void deleteById(Integer id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Lab not found"));

        for (User user : lab.getUsers()) {
            user.getLabs().remove(lab);
        }
        lab.getUsers().clear();

        labRepository.delete(lab);
    }
}
