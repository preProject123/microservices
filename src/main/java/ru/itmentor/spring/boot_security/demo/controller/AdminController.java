package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "table";
    }

    @GetMapping("/users/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("rolesList", roleRepository.findAll());
        return "create";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam List<String> rolesList) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = rolesList.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("rolesList", roleRepository.findAll());
        return "edit";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User userDetails, @RequestParam List<String> rolesList) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        Set<Role> roles = rolesList.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        for (String roleName : rolesList) {
            roleRepository.findByName(roleName).ifPresent(roles::add);
        }
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}