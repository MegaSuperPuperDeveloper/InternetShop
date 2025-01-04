package com.example.controller;

import com.example.enums.Tag;
import com.example.model.Product;
import com.example.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //region READ
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/i/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/u/{name}")
    public ResponseEntity<List<Product>> getProductByProductId(@PathVariable String name) {
        List<Product> products = productService.getProductsByName(name);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    //endregion

    @PostMapping("/{name}/{description}/{price}/{tag}")
    public ResponseEntity<Product> addProduct(@PathVariable String name,
                                              @PathVariable String description,
                                              @PathVariable BigDecimal price,
                                              @PathVariable Tag tag) {
        return new ResponseEntity<>(productService.addProduct(name, description, price, tag), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteProductById(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //region UPDATE
    @PatchMapping("/{productId}/n/{name}")
    public ResponseEntity<Void> updateNameById(@PathVariable Long productId, @PathVariable String name) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateNameById(productId, name);
        productService.updateUpdatedAt(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/d/{description}")
    public ResponseEntity<Void> updateDescriptionById(@PathVariable Long productId, @PathVariable String description) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateDescriptionById(productId, description);
        productService.updateUpdatedAt(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/p/{price}")
    public ResponseEntity<Void> updatePriceById(@PathVariable Long productId, @PathVariable double price) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updatePriceById(productId, price);
        productService.updateUpdatedAt(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/t/{tag}")
    public ResponseEntity<Void> updateTagById(@PathVariable Long productId, @PathVariable Tag tag) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateTagById(productId, tag);
        productService.updateUpdatedAt(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion

}