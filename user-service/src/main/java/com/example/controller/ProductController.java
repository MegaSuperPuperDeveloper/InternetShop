package com.example.controller;

import com.example.enums.Tag;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.ProductService;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    //region READ
    @GetMapping
    public String getAllProducts(Model model) {
        productService.waitASecond();
        model.addAttribute("products", productService.getProducts());
        return "/products/products";
    }

    @GetMapping("/u/{name}")
    public String getProductByName(@PathVariable String name, Model model) {
        productService.waitASecond();
        model.addAttribute("products", productService.getProductsByName(name));
        return "/products/products";
    }

    @GetMapping("/i/{productId}")
    public String findById(@PathVariable Long productId, Model model) {
        productService.waitASecond();
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            return "/products/productDoesNotExist";
        }
        model.addAttribute("product", product.orElse(null));
        return "/products/product";
    }
    //endregion

    //region Publish product
    @GetMapping("/publishProduct")
    public String publishProduct() {
        productService.waitASecond();
        return "/products/publishProduct";
    }

    @GetMapping("/publishProductById")
    public String publishProductById(@AuthenticationPrincipal User user,
                                     @RequestParam String name,
                                     @RequestParam String description,
                                     @RequestParam BigDecimal price,
                                     @RequestParam Tag tag) {
        Product product = productService.addProduct(name, description, price, tag, user.getDisplayedUsername(), user.getId());
        return "redirect:/products/i/" + product.getId();
    }
    //endregion

    //region Delete
    @GetMapping("/deleteProduct")
    public String deleteProduct() {
        productService.waitASecond();
        return "/products/deleteProduct";
    }

    @PostMapping("/deleteProductById")
    public String deleteProductById(@AuthenticationPrincipal User user, @RequestParam Long productId, @RequestParam String password) {
        if (productService.findById(productId).isEmpty()) {
            return "/products/productDoesNotExist";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (!productService.findById(productId).get().getAuthorId().equals(user.getId())) {

            int owner = userService.findById(productService.findById(productId).get().getAuthorId()).get().role().getHierarchy();
            int userWhoWantsDeleteProduct = userService.findById(user.getId()).get().role().getHierarchy();

            if (userWhoWantsDeleteProduct <= owner) {
                return "/users/youAreNotHigher";
            }

        }
        productService.deleteProductById(productId);
        return "redirect:/products";
    }
    //endregion

    //region Name change
    @GetMapping("/updateName")
    public String updateYourUsername() {
        productService.waitASecond();
        return "/products/updateName";
    }

    @PostMapping("/updateNameById")
    public String updateYourUsernameById(@AuthenticationPrincipal User user,
                                         @RequestParam String password, @RequestParam String newName, @RequestParam Long productId) {
        if (productService.findById(productId).isEmpty()) {
            return "/products/productDoesNotExist";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (!productService.findById(productId).get().getAuthorId().equals(user.getId())) {

            int owner = userService.findById(productService.findById(productId).get().getAuthorId()).get().role().getHierarchy();
            int userWhoWantsDeleteProduct = userService.findById(user.getId()).get().role().getHierarchy();

            if (userWhoWantsDeleteProduct <= owner) {
                return "/users/youAreNotHigher";
            }
            productService.updateNameById(productId, "Name");
            return "redirect:/products/i/" + productId;

        }
        productService.updateNameById(productId, newName);
        return "redirect:/products/i/" + productId;
    }
    //endregion

    //region Description
    @GetMapping("/updateDescription")
    public String updateDescription() {
        productService.waitASecond();
        return "/products/updateDescription";
    }

    @PostMapping("/updateDescriptionById")
    public String updateDescriptionById(@AuthenticationPrincipal User user,
                                        @RequestParam String newDescription, @RequestParam Long productId, @RequestParam String password) {
        if (productService.findById(productId).isEmpty()) {
            return "/products/productDoesNotExist";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (!productService.findById(productId).get().getAuthorId().equals(user.getId())) {

            int owner = userService.findById(productService.findById(productId).get().getAuthorId()).get().role().getHierarchy();
            int userWhoWantsDeleteProduct = userService.findById(user.getId()).get().role().getHierarchy();

            if (userWhoWantsDeleteProduct <= owner) {
                return "/users/youAreNotHigher";
            }
            productService.updateDescriptionById(productId, "Description");
            return "redirect:/products/i/" + productId;

        }
        productService.updateDescriptionById(productId, newDescription);
        return "redirect:/products/i/" + productId;
    }
    //endregion

    //region Price change (СДЕЛАТЬ ВЫБОР ВАЛЮТЫ)
    @GetMapping("/updatePrice")
    public String updatePrice() {
        productService.waitASecond();
        return "/products/updatePrice";
    }

    @PostMapping("/updatePriceById")
    public String updatePriceById(@AuthenticationPrincipal User user,
                                  @RequestParam String password, @RequestParam Long productId, @RequestParam BigDecimal newPrice) {
        if (productService.findById(productId).isEmpty()) {
            return "/products/productDoesNotExist";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        if (!productService.findById(productId).get().getAuthorId().equals(user.getId())) {
            return "/products/youCannotChangeIt";
        }
        productService.updatePriceById(productId, newPrice);
        return "redirect:/products/i/" + productId;
    }
    //endregion

    //region Tag change
    @GetMapping("/updateTag")
    public String updateTag() {
        productService.waitASecond();
        return "/products/updateTag";
    }

    @PostMapping("/updateTagById")
    public String updateTagById(@AuthenticationPrincipal User user,
                                @RequestParam String password, @RequestParam Long productId, @RequestParam Tag tag) {
        if (productService.findById(productId).isEmpty()) {
            return "/products/productDoesNotExist";
        }
        if (!productService.findById(productId).get().getAuthorId().equals(user.getId())) {
            return "/products/youCannotChangeIt";
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "/users/passwordsDoNotMatch";
        }
        productService.updateTagById(productId, tag);
        return "redirect:/products/i/" + productId;
    }
    //endregion

}