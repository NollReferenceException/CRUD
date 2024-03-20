package com.example.crud.service;

import com.example.crud.dao.RoleRepository;
import com.example.crud.model.Role;
import com.example.crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleDao;

    public Role findByName(String role) {
        try {
            return roleDao.findByRole(role).orElseThrow(() -> new RoleNotFoundException(role + " not found"));
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Role role) {
        roleDao.save(role);
    }

    public Set<Role> findAll() {
        return new HashSet<>(roleDao.findAll());
    }
}
