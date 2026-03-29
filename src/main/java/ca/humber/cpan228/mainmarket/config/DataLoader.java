package ca.humber.cpan228.mainmarket.config;

import ca.humber.cpan228.mainmarket.entity.Role;
import ca.humber.cpan228.mainmarket.entity.User;
import ca.humber.cpan228.mainmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create demo users if they don't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@mainmarket.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .role(Role.ADMIN)
                    .isEnabled(true)
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .build();
            userRepository.save(admin);
            System.out.println("✓ Admin user created - Username: admin, Password: admin123");
        }

        if (!userRepository.existsByUsername("staff")) {
            User staff = User.builder()
                    .username("staff")
                    .email("staff@mainmarket.com")
                    .password(passwordEncoder.encode("staff123"))
                    .firstName("Staff")
                    .lastName("User")
                    .role(Role.STAFF)
                    .isEnabled(true)
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .build();
            userRepository.save(staff);
            System.out.println("✓ Staff user created - Username: staff, Password: staff123");
        }

        if (!userRepository.existsByUsername("customer")) {
            User customer = User.builder()
                    .username("customer")
                    .email("customer@mainmarket.com")
                    .password(passwordEncoder.encode("customer123"))
                    .firstName("John")
                    .lastName("Customer")
                    .role(Role.CUSTOMER)
                    .isEnabled(true)
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .build();
            userRepository.save(customer);
            System.out.println("✓ Customer user created - Username: customer, Password: customer123");
        }

        System.out.println("✓ User initialization complete");
    }
}
