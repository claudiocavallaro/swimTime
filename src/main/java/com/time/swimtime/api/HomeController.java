package com.time.swimtime.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "HOME CONTROLLER";
    }

}
