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
import java.util.Optional;

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
    public ResponseEntity<Optional<Product>> findById(@PathVariable Long productId) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productService.findById(productId), HttpStatus.OK);
    }

    @GetMapping("/u/{name}")
    public ResponseEntity<List<Product>> getProductByProductId(@PathVariable String name) {
        productService.waitASecond();
        if (productService.getProductsByName(name).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productService.getProductsByName(name), HttpStatus.OK);
    }
    //endregion

    @PostMapping("/{name}/{description}/{price}/{tag}")
    public ResponseEntity<Product> addProduct(@PathVariable String name,
                                              @PathVariable String description,
                                              @PathVariable BigDecimal price,
                                              @PathVariable Tag tag) {
        productService.waitASecond();
        return new ResponseEntity<>(productService.addProduct(name, description, price, tag), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteProductById(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //region UPDATE
    @PatchMapping("/{productId}/n/{name}")
    public ResponseEntity<Void> updateNameById(@PathVariable Long productId, @PathVariable String name) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateNameById(productId, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/d/{description}")
    public ResponseEntity<Void> updateDescriptionById(@PathVariable Long productId, @PathVariable String description) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateDescriptionById(productId, description);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/p/{price}")
    public ResponseEntity<Void> updatePriceById(@PathVariable Long productId, @PathVariable double price) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updatePriceById(productId, price);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/t/{tag}")
    public ResponseEntity<Void> updateTagById(@PathVariable Long productId, @PathVariable Tag tag) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateTagById(productId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion

}