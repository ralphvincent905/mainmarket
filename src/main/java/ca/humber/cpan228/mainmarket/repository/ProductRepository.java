package ca.humber.cpan228.mainmarket.repository;

import ca.humber.cpan228.mainmarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByBrand_BrandId(Long brandId, Pageable pageable);

    Page<Product> findByCategory_CategoryIdAndBrand_BrandId(
        Long categoryId, Long brandId, Pageable pageable
    );
}
