package com.example.controller;

import com.example.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

}