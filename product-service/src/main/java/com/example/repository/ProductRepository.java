package com.example.repository;

import com.example.enums.Tag;
import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Iterable<Product> findProductByName(String name);

    @Modifying
    @Query("UPDATE Product p SET p.updatedAt = :updatedAt WHERE p.id = :id")
    void updateUpdatedAt(Long id, LocalDateTime updatedAt);

    @Modifying
    @Query("UPDATE Product p SET p.name = :name WHERE p.id = :id")
    void updateNameById(Long id, String name);

    @Modifying
    @Query("UPDATE Product p SET p.description = :description WHERE p.id = :id")
    void updateDescriptionById(Long id, String description);

    @Modifying
    @Query("UPDATE Product p SET p.price = :price WHERE p.id = :id")
    void updatePriceById(Long id, String price);

    @Modifying
    @Query("UPDATE Product p SET p.tags = :tags WHERE p.id = :id")
    void updateTagsById(Long id, Set<Tag> tags);

    @Modifying
    @Query("UPDATE Product p SET p.count = :count WHERE p.id = :id")
    void updateCountById(Long id, String count);

}