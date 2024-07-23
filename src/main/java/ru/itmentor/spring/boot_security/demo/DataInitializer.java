package ru.itmentor.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@Component
public class DataInitializer {

    @Autowired
    private UserService userService;

}