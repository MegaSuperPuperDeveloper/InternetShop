package com.example.service;

import com.example.enums.Tag;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //region READ
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findProductByName(name);
    }
    //endregion

    public Product addProduct(String name, String description, BigDecimal price, Tag tag) {
        return productRepository.save(new Product(name, description, price, tag));
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    //region UPDATE
    @Transactional
    public void updateUpdatedAt(Long id) {
        productRepository.updateUpdatedAt(id, LocalDateTime.now());
    }

    @Transactional
    public void updateNameById(Long id, String name) {
        productRepository.updateNameById(id, name);
    }

    @Transactional
    public void updateDescriptionById(Long id, String description) {
        productRepository.updateDescriptionById(id, description);
    }

    @Transactional
    public void updatePriceById(Long id, double price) {
        productRepository.updatePriceById(id, price);
    }

    @Transactional
    public void updateTagById(Long id, Tag tag) {
        productRepository.updateTagById(id, tag);
    }
    //endregion

}