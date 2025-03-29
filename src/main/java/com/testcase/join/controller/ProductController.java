package com.testcase.join.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testcase.join.dto.ApiResponseDto;
import com.testcase.join.dto.ProductDto;
import com.testcase.join.dto.ProductFilterDto;
import com.testcase.join.service.ProductService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves a list of products based on the provided filter criteria.
     *
     * @param byName     the product name
     * @param byCategory the product category
     * @param byBrand    the product brand
     * @param byMinPrice the minimum product price
     * @param byMaxPrice the maximum product price
     * @param limit      the maximum number of products to be returned
     * @param page       the page number (1-indexed)
     * @param pageSize   the page size
     * @return a ResponseEntity containing the list of products, or an empty list if
     *         no products are found
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ProductDto>>> getProducts(
            @RequestParam(required = false, defaultValue = "") String byName,
            @RequestParam(required = false, defaultValue = "") String byCategory,
            @RequestParam(defaultValue = "") String byBrand,
            @RequestParam(required = false, defaultValue = "1,00") double byMinPrice,
            @RequestParam(required = false, defaultValue = "1000,00") double byMaxPrice,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        ProductFilterDto productFilterDto = new ProductFilterDto(
                Optional.ofNullable(byName),
                Optional.ofNullable(byCategory),
                Optional.ofNullable(byBrand),
                Optional.ofNullable(byMinPrice),
                Optional.ofNullable(byMaxPrice),
                Optional.ofNullable(limit),
                Optional.ofNullable(page),
                Optional.ofNullable(pageSize));

        return new ResponseEntity<>(
                new ApiResponseDto<List<ProductDto>>(200, "Success",
                        productService.getProducts(productFilterDto)),
                HttpStatus.OK);
    }

}
