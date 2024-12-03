package ru.itmo.tg.springbootcrud.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DefaultAdminPreLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${credentials.admin.username}")
    private String adminUsername;

    @Value("${credentials.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

}
