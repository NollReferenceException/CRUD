package com.example.crud.setups;

import com.example.crud.dao.RoleRepository;
import com.example.crud.dao.UserRepository;
import com.example.crud.model.Role;
import com.example.crud.model.User;
import com.example.crud.service.RoleServiceImpl;
import com.example.crud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        alreadySetup = true;
    }

    @Transactional
    public Role createRoleIfNotFound(String name) {

        Optional<Role> optionalRole = roleRepository.findByRole(name);

        return optionalRole.orElseGet(() -> roleRepository.save(new Role(name)));
    }
}