package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(
        name = "uk_category_user_name_type", columnNames = {"user_id", "name", "type"}
))
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    protected CategoryEntity() {}

    public CategoryEntity(String name, CategoryType type, UserEntity user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }
}
