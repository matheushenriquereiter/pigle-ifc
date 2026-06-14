package io.github.matheushenriquereiter.project.service;

import io.github.matheushenriquereiter.project.dto.UserDTO;
import io.github.matheushenriquereiter.project.model.User;
import io.github.matheushenriquereiter.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getUsers() {
        List<User> userList = userRepository.findAll();

        return userList.stream().map(User::toDTO).toList();
    }

    public UserDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            return null;
        }

        return user.toDTO();
    }

    public void register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("UserDTO is null");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public boolean validateCredentials(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public void updateUser(UserDTO userDTO, User updatedUser) {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("UserDTO is null");
        }
        User user = userOptional.get();

        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());

        userRepository.save(user);
    }
}
