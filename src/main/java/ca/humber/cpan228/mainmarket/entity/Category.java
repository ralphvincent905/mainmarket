package ca.humber.cpan228.mainmarket.entity;

import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private Integer displayOrder = 0;
    private Boolean isActive = true;

}
