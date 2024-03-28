package com.example.crud.controllers;

import com.example.crud.model.User;
import com.example.crud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.example.crud.util.UserDataException;
import com.example.crud.util.UserErrorResponse;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/user")
    public ModelAndView showUserPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user.html");
        return modelAndView;
    }

    @RequestMapping("/admin")
    public ModelAndView showPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping("/register")
    public ModelAndView showRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register.html");
        return modelAndView;
    }

    @PostMapping("/register")
    public User addUser(@RequestBody User user, BindingResult result) {

        errorHandling(result);
        return userService.save(user);
    }

    @GetMapping("/user/data")
    public User showUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/admin/users")
    public Iterable<User> showUserList() {
        return userService.findAll();
    }

    @PutMapping("/admin/users")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user, BindingResult result) {
        errorHandling(result);
        userService.update(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("admin/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    private void errorHandling(BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new UserDataException(errorMsg.toString());
        }
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserDataException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
