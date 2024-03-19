package com.example.crud.controllers;

import com.example.crud.model.User;
import com.example.crud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/registration")
    public String showRegistrationForm(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/registration";
        }

        userService.save(user);

        return "redirect:/login";
    }
}
