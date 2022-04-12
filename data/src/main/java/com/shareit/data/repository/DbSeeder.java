package com.shareit.data.repository;

import com.shareit.domain.entity.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"test", "dev"})
public class DbSeeder implements CommandLineRunner {
    private final UserRepository userRepository;

    private final String strategy;


    public DbSeeder(UserRepository userRepository, Environment environment) {
        this.userRepository = userRepository;
        this.strategy = environment.getProperty("spring.jpa.hibernate.ddl-auto","none");
    }

    @Override
    public void run(String... args) throws Exception {
        if(!"create".equals(strategy)) {
            return;
        }
        userRepository.save(new UserEntity("test@gmail.com", "password", "test", LocalDate.now()));
    }
}
