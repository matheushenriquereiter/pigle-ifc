package io.github.matheushenriquereiter.project.controller;

import io.github.matheushenriquereiter.project.dto.UserDTO;
import io.github.matheushenriquereiter.project.model.LoginForm;
import io.github.matheushenriquereiter.project.model.RegistrationForm;
import io.github.matheushenriquereiter.project.service.JwtService;
import io.github.matheushenriquereiter.project.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/list-users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getUsers());

        return "list-users";
    }

    @GetMapping("/show-user")
    public String showUser(Model model, Principal principal) {
        String loggedUserEmail = principal.getName();
        UserDTO loggedUser = userService.getByEmail(loggedUserEmail).toDTO();
        model.addAttribute("loggedUser", loggedUser);

        return "show-user";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        RegistrationForm registrationForm = new RegistrationForm();
        model.addAttribute("registrationForm", registrationForm);

        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        UserDTO userDTO = new UserDTO(registrationForm.getName(), registrationForm.getEmail(), registrationForm.getPassword());
        userService.save(userDTO.toEntity());

        redirectAttributes.addFlashAttribute("registrationForm", registrationForm);

        return "redirect:register-success";
    }

    @GetMapping("/register-success")
    public String registerSuccess() {
        return "register-success";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);

        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpServletResponse response, Model model) {
        if (result.hasErrors()) {
            return "login";
        }

        boolean validatedCredentials = userService.validateCredentials(loginForm.getEmail(), loginForm.getPassword());

        if (!validatedCredentials) {
            model.addAttribute("loginError", "Incorrect email address or password.");
            return "login";
        }

        String token = jwtService.generateToken(loginForm.getEmail());

        Cookie jwtCookie = new Cookie("jwt_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);

        response.addCookie(jwtCookie);

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
