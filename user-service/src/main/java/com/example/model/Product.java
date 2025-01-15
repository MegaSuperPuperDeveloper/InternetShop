package com.example.model;

import com.example.enums.Currency;
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
    private String price;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Tag tag;

    @Column(nullable = false, name = "author_name")
    private String authorName;

    @Column(nullable = false, name = "author_id")
    private Long authorId;

    @Column(nullable = false, name = "phone_number")
    private String authorPhoneNumber;

    public Product(String name, String description, String price, Currency currency, Tag tag, String authorName, Long authorId, String authorPhoneNumber) {
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.price = price + currency.getIcon();
        this.tag = tag;
        this.createdAt = LocalDateTime.now();
        this.authorName = authorName;
        this.authorId = authorId;
        this.authorPhoneNumber = authorPhoneNumber;
    }

}