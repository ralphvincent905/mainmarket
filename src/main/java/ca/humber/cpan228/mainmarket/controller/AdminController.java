package ca.humber.cpan228.mainmarket.controller;

import ca.humber.cpan228.mainmarket.entity.Role;
import ca.humber.cpan228.mainmarket.repository.ProductRepository;
import ca.humber.cpan228.mainmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();

        long adminCount = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .count();
        long staffCount = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.STAFF)
                .count();
        long customerCount = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.CUSTOMER)
                .count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("staffCount", staffCount);
        model.addAttribute("customerCount", customerCount);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("products", productRepository.findAll());

        return "admin-dashboard";
    }
}