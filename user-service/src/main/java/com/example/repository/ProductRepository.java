package com.example.repository;

import com.example.enums.Tag;
import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findProductByName(String name);

    @Modifying
    @Query("UPDATE Product p SET p.name = :name WHERE p.id = :id")
    void updateNameById(Long id, String name);

    @Modifying
    @Query("UPDATE Product p SET p.description = :description WHERE p.id = :id")
    void updateDescriptionById(Long id, String description);

    @Modifying
    @Query("UPDATE Product p SET p.price = :price WHERE p.id = :id")
    void updatePriceById(Long id, double price);

    @Modifying
    @Query("UPDATE Product p SET p.tag = :tag WHERE p.id = :id")
    void updateTagById(Long id, Tag tag);

}