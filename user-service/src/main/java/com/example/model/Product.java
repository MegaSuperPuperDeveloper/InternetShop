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

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private Tag tag;

    @Column(nullable = false, name = "author_name")
    private String authorName;

    @Column(nullable = false, name = "author_id")
    private Long authorId;

    public Product(String name, String description, BigDecimal price, Tag tag, String authorName, Long authorId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.tag = tag;
        this.createdAt = createdAt();
        this.authorName = authorName;
        this.authorId = authorId;
    }

    public String createdAt() {
        String[] array = LocalDateTime.now().toString().split("T");
        array = array[1].split("\\.");
        return array[0] + ", " + array[1];
    }

}