package com.example.service;

import com.example.enums.Currency;
import com.example.enums.Tag;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //region READ
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByTag(Set<Tag> tags) {
        return Stream.concat(
                productRepository.findAll()
                        .stream()
                        .filter(p -> tags.contains(p.getTag())),
                productRepository.findAll()
                        .stream()
                        .filter(p -> !tags.contains(p.getTag()))
        ).collect(Collectors.toList());
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findProductByName(name);
    }
    //endregion

    public Product addProduct(String name, String description, String price, Currency currency, Tag tag, String authorName, Long authorId, String authorPhoneNumber) {
        return productRepository.save(new Product(name, description, price, currency, tag, authorName, authorId, authorPhoneNumber));
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    //region UPDATE
    @Transactional
    public void updateNameById(Long id, String name) {
        productRepository.updateNameById(id, name);
    }

    @Transactional
    public void updateDescriptionById(Long id, String description) {
        productRepository.updateDescriptionById(id, description);
    }

    @Transactional
    public void updatePhoneNumberById(Long id, String authorPhoneNumber) {
        productRepository.updatePhoneNumberById(id, authorPhoneNumber);
    }

    @Transactional
    public void updatePriceById(Long id, BigDecimal price) {
        productRepository.updatePriceById(id, price);
    }

    @Transactional
    public void updateTagById(Long id, Tag tag) {
        productRepository.updateTagById(id, tag);
    }
    //endregion

    //region Other functions
    public void waitASecond() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Не удалось подождать секунду!");
        }
    }
    //endregion

}