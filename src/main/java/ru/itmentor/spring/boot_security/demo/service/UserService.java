package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public void createUserIfNotExists(String username, String password, String roleName) {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));  // Хэширование пароля

            Optional<Role> role = roleRepository.findByName(roleName);
            Set<Role> roles = new HashSet<>();
            if (role.isPresent()) {
                roles.add(role.get());
            } else {
                Role newRole = new Role(roleName);
                roleRepository.save(newRole);
                roles.add(newRole);
            }
            user.setRoles(roles);

            userRepository.save(user);
        } else {
            System.out.println("User with username " + username + " already exists.");
        }
    }
}