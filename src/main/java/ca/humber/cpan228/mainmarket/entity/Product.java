package ca.humber.cpan228.mainmarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @DecimalMin("0.01")
    private Double price;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    private Double weight;
    private String dimensions;
    private String color;
    private String size;

    @Builder.Default
    private Boolean isAvailable = true;
    @Builder.Default
    private Boolean isFeatured = false;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}

