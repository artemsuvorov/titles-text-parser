package application.controllers;

import application.data.UserRepository;
import application.models.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class HomeController {

    private final UserRepository repository = new UserRepository();

    @GetMapping("/home")
    public String getHomeHelloString(@RequestParam(value = "name", defaultValue = "User") String name) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var today = LocalDateTime.now().format(formatter);
        return "Hello, %s! Today is: %s".formatted(name, today);
    }

    @GetMapping("/user")
    public User getUser(@RequestParam(value = "name", required = true) String name) {
        return repository.getUserOrNull(name);
    }

    @PostMapping("/user")
    public User updateUser(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "age", required = true) int age) {
        var user = new User(name, age);
        repository.addOrUpdateUser(user);
        return user;
    }

}