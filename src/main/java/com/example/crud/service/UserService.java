package com.example.crud.service;

import com.example.crud.dao.UserRepository;
import com.example.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userDao;


    public void save(User user) {
        userDao.save(user);
    }

    public Iterable<User> findAll() {
        return userDao.findAll();
    }

    public User findById(long id) {
        return userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    public void delete(User user) {
        userDao.delete(user);
    }
}
