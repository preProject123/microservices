package com.task314;

import com.task314.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
public class Task314Application {

    private static final String API_URL = "http://94.198.50.185:7081/api/users";
    private static String sessionId;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);
        sessionId = response.getHeaders().get("Set-Cookie").get(0);

        User newUser = new User(3L, "James", "Brown", (byte) 30);
        headers.set("Cookie", sessionId);
        HttpEntity<User> entitySave = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> responseSave =
                restTemplate.exchange(API_URL, HttpMethod.POST, entitySave, String.class);

        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 30);
        HttpEntity<User> entityUpdate = new HttpEntity<>(updatedUser, headers);
        ResponseEntity<String> responseUpdate =
                restTemplate.exchange(API_URL, HttpMethod.PUT, entityUpdate, String.class);

        HttpEntity<String> entityDelete = new HttpEntity<>(headers);
        ResponseEntity<String> responseDelete =
                restTemplate.exchange(API_URL + "/3", HttpMethod.DELETE, entityDelete, String.class);

        String code = responseSave.getBody() + responseUpdate.getBody() + responseDelete.getBody();
        System.out.println(code);
        System.out.println("Количество строк: " + code.length());
    }
}