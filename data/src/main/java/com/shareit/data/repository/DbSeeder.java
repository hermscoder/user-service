package com.shareit.data.repository;

import com.shareit.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DbSeeder implements CommandLineRunner {
    private UserRepository userRepository;

    public DbSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User(null, "test@gmail.com", "password", "test", LocalDate.now()));
    }
}
