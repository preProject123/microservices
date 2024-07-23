package ru.itmentor.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(){
		return args -> {
			userService.createUserIfNotExists("user", "password", "ROLE_USER");
			userService.createUserIfNotExists("admin", "password", "ROLE_ADMIN");
		};
	}

}
