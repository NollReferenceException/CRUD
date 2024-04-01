package com.example.crud.setup;

import com.example.crud.dao.RoleRepository;
import com.example.crud.model.Role;
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

        createRoleIfNotFound("ROLE_ADMIN", 1);
        createRoleIfNotFound("ROLE_USER", 2);

        alreadySetup = true;
    }

    @Transactional
    public Role createRoleIfNotFound(String name, long id) {

        Optional<Role> optionalRole = roleRepository.findByRole(name);

        return optionalRole.orElseGet(() -> roleRepository.save(new Role(name, id)));
    }
}