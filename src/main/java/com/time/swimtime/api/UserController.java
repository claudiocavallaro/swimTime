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

    @GetMapping("/findUser")
    public String findUser(@RequestParam String nome) {
        return userService.find(nome);
    }


    @GetMapping("/findTime")
    public String findTime(@RequestParam String id) {
        return userService.findTime(id);
    }

}
