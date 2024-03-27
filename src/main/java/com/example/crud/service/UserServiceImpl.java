package com.example.crud.service;

import com.example.crud.dao.UserRepository;
import com.example.crud.model.Role;
import com.example.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user) {
        if (userDao.findByName(user.getName()).isPresent()) {
            throw new RuntimeException(user.getName() + " пользователь уже существует");
        }

        user.setRoles(Set.of(roleService.findByName("ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public void update(User user) {
        if (user.getPassword().isEmpty()) {
            user.setPassword(userDao.findById(user.getId()).get().getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(userDao.findById(user.getId()).get().getRoles());
        } else {
            Set<Role> roles = user.getRoles().stream()
                    .map(s -> roleService.findByName(s.getRole()))
                    .collect(Collectors.toSet());

            user.setRoles(roles);
        }

        userDao.save(user);
    }

    public Iterable<User> findAll() {
        List<User> all = userDao.findAll();
        all.forEach(el -> el.setAllRoles(roleService.findAll()));
        return all;
    }

    public User findById(long id) {
        return userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    public void delete(User user) {
        user.setRoles(new HashSet<>());
        userDao.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByName(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
