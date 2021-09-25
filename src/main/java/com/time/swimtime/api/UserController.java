package com.time.swimtime.api;

import com.time.swimtime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/find")
    public String findAll(@RequestParam String nome) {
        return userService.find(nome);
    }
}
