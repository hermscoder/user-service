package com.shareit.data.repository;

import com.shareit.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"test", "dev"})
public class DbSeeder implements CommandLineRunner {
    private final UserRepository userRepository;

    private final String strategy;

    public DbSeeder(UserRepository userRepository, @Value(value = "${spring.jpa.hibernate.ddl-auto:none}") String strategy) {
        this.userRepository = userRepository;
        this.strategy = strategy;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!"create".equals(strategy)) {
            return;
        }
        userRepository.save(new User("test@gmail.com", "password", "test", LocalDate.now()));
    }
}
