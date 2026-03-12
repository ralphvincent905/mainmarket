package ca.humber.cpan228.mainmarket.repository;

import ca.humber.cpan228.mainmarket.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
