package com.example.crud.controllers;

import com.example.crud.model.User;
import com.example.crud.service.RoleServiceImpl;
import com.example.crud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping("/user")
    public User showUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/admin")
    public Iterable<User> showUserList() {
        return userService.findAll();
    }

    @PostMapping("/admin/create")
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user, BindingResult result) {

        userService.save(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @GetMapping("/admin/create")
//    public String showUserCreationForm(@ModelAttribute("user") User user) {
//        return "create-user";
//    }

//    @GetMapping("/admin/edit/{id}")
//    public String showUpdateForm(@PathVariable("id") long id, Model model) {
//        User user = userService.findById(id);
//        user.setAllRoles(roleService.findAll());
//
//        model.addAttribute("user", user);
//        return "update-user";
//    }

    @PostMapping("admin/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user, BindingResult result) {

        userService.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("admin/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        User user = userService.findById(id);
        userService.delete(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
