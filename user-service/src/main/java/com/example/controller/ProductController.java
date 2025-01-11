package com.example.controller;

import com.example.enums.Tag;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.ProductService;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    //region READ
    @GetMapping
    public String getAllProducts(Model model) {
        productService.waitASecond();
        model.addAttribute("products", productService.getProducts());
        return "products";
    }

    @GetMapping("/i/{productId}")
    public String findById(@PathVariable Long productId, Model model) {
        productService.waitASecond();
        model.addAttribute("products", productService.findById(productId).stream().collect(Collectors.toList()));
        return "products";
    }

    @GetMapping("/u/{name}")
    public String getProductByName(@PathVariable String name, Model model) {
        productService.waitASecond();
        model.addAttribute("products", productService.getProductsByName(name));
        return "products";
    }
    //endregion

    @PostMapping("/{name}/{description}/{price}/{tag}")
    public ResponseEntity<Product> addProduct(@AuthenticationPrincipal User user,
                                              @PathVariable String name,
                                              @PathVariable String description,
                                              @PathVariable BigDecimal price,
                                              @PathVariable Tag tag) {
        productService.waitASecond();
        return new ResponseEntity<>(productService.addProduct(name, description, price, tag, user.getDisplayedUsername(), user.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!productService.findById(productId).get().getAuthorId().equals(user.getId())) {

            int owner = userService.findById(productService.findById(productId).get().getAuthorId()).get().role().getHierarchy();
            int userWhoWantsDeleteProduct = userService.findById(user.getId()).get().role().getHierarchy();

            if (userWhoWantsDeleteProduct <= owner) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        productService.deleteProductById(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //region UPDATE
    @PatchMapping("/{productId}/n/{name}")
    public ResponseEntity<Void> updateNameById(@AuthenticationPrincipal User user,
                                               @PathVariable Long productId, @PathVariable String name) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateNameById(productId, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/d/{description}")
    public ResponseEntity<Void> updateDescriptionById(@AuthenticationPrincipal User user,
                                                      @PathVariable Long productId, @PathVariable String description) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateDescriptionById(productId, description);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/p/{price}")
    public ResponseEntity<Void> updatePriceById(@AuthenticationPrincipal User user,
                                                @PathVariable Long productId, @PathVariable double price) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updatePriceById(productId, price);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}/t/{tag}")
    public ResponseEntity<Void> updateTagById(@AuthenticationPrincipal User user,
                                              @PathVariable Long productId, @PathVariable Tag tag) {
        productService.waitASecond();
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateTagById(productId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion

}