package com.example.service;

import com.example.enums.Tag;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //region READ
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    public Iterable<Product> getProductsByName(String name) {
        return productRepository.findProductByName(name);
    }
    //endregion

    public Product addProduct(String name, String description, double price, Set<Tag> tags, int count) {
        return productRepository.save(new Product(name, description, price, tags, count));
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    //region UPDATE

    public void updateUpdatedAt(Long id) {
        productRepository.updateUpdatedAt(id, LocalDateTime.now());
    }

    public void updateNameById(Long id, String name) {
        productRepository.updateNameById(id, name);
    }

    public void updateDescriptionById(Long id, String description) {
        productRepository.updateDescriptionById(id, description);
    }

    public void updatePriceById(Long id, String price) {
        productRepository.updatePriceById(id, price);
    }

    public void updateTagsById(Long id, Set<Tag> tags) {
        productRepository.updateTagsById(id, tags);
    }

    public void updateCountById(Long id, String count) {
        productRepository.updateCountById(id, count);
    }

    //endregion

}