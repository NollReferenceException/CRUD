package com.example.crud.controllers;

import com.example.crud.model.User;
import com.example.crud.service.RoleServiceImpl;
import com.example.crud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping("/user")
    public String showUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("users", Collections.singletonList(user));
        return "user";
    }

    @GetMapping("/admin")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "adminPanel";
    }

    @PostMapping("/admin/create")
    public String addUser(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "create-user";
        }

        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/create")
    public String showUserCreationForm(@ModelAttribute("user") User user) {
        return "create-user";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        user.setAllRoles(roleService.findAll());

        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("admin/update/{id}")
    public String updateUser(@PathVariable("id") long id, User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userService.findById(id);

        userService.delete(user);
        return "redirect:/admin";
    }
}
