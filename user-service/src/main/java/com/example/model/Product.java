package com.example.model;

import com.example.enums.Tag;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Table(name = "products")
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Tag tag;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private Long authorId;

    public Product(String name, String description, BigDecimal price, Tag tag, String authorName, Long authorId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.tag = tag;
        this.authorName = authorName;
        this.authorId = authorId;
    }

}