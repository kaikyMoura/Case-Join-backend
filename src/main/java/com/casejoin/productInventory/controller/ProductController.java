package com.casejoin.productInventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casejoin.productInventory.dto.ApiResponseDto;
import com.casejoin.productInventory.dto.ProductDto;
import com.casejoin.productInventory.dto.ProductFilterDto;
import com.casejoin.productInventory.exception.CustomNotFoundException;
import com.casejoin.productInventory.exception.RequiredArgumentsMissing;
import com.casejoin.productInventory.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/product")
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
         * @throws CustomNotFoundException 
         */
        @Operation(summary = "Get all products or filtered")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Products found", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Products not found", content = @Content) })
        @GetMapping
        public ResponseEntity<ApiResponseDto<List<ProductDto>>> getProducts(
                        @Parameter(description = "filter by product name", required = false) @RequestParam(defaultValue = "") String name,
                        @Parameter(description = "filter by product category", required = false) @RequestParam(defaultValue = "") String category,
                        @Parameter(description = "filter by product brand", required = false) @RequestParam(defaultValue = "") String brand,
                        @Parameter(description = "filter by product min price", required = false) @RequestParam(required = false, defaultValue = "1,00") String minPrice,
                        @Parameter(description = "filter by product max price", required = false) @RequestParam(required = false, defaultValue = "1000,00") String maxPrice,
                        @Parameter(description = "add pagination", required = false) @RequestParam(required = false, defaultValue = "1") int page,
                        @Parameter(description = "add pagination", required = false) @RequestParam(required = false, defaultValue = "10") int pageSize) throws CustomNotFoundException {

                ProductFilterDto productFilterDto = new ProductFilterDto(
                                name,
                                category,
                                brand,
                                Double.parseDouble(minPrice.replace(",", ".")),
                                Double.parseDouble(maxPrice.replace(",", ".")),
                                page,
                                pageSize);

                return new ResponseEntity<>(
                                new ApiResponseDto<List<ProductDto>>(200, null,
                                                productService.getProducts(productFilterDto)),
                                HttpStatus.OK);
        }

        /**
         * Retrieves a product based on the provided product ID.
         *
         * @param id the product ID to retrieve
         * @return a ResponseEntity containing the product, or an empty response if the
         *         product
         *         is not found
         * @throws RequiredArgumentsMissing if the product ID is not provided
         * @throws CustomNotFoundException  if the product is not found
         */
        @Operation(summary = "Get product by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content) })
        @GetMapping("{id}")
        public ResponseEntity<ApiResponseDto<ProductDto>> getProductById(
                        @Parameter(description = "product id", required = true) @RequestParam(required = true) UUID id)
                        throws RequiredArgumentsMissing, CustomNotFoundException {

                return new ResponseEntity<>(
                                new ApiResponseDto<ProductDto>(200, "Product retrieved successfully",
                                                productService.getProduct(id)),
                                HttpStatus.OK);
        }

        /**
         * Creates a new product based on the provided product DTO.
         *
         * @param productDto the product details to be created
         * @return a ResponseEntity containing the status of the creation operation
         * @throws RequiredArgumentsMissing if the product DTO is not provided
         * @throws CustomNotFoundException  if the creation fails due to any other
         *                                  reason
         */
        @Operation(summary = "Create a new product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content) })
        @PostMapping
        public ResponseEntity<ApiResponseDto<String>> createProduct(
                        @Parameter(description = "product id", required = true) @RequestBody(required = true) ProductDto productDto)
                        throws RequiredArgumentsMissing, CustomNotFoundException {

                productService.createProduct(productDto);
                return new ResponseEntity<>(
                                new ApiResponseDto<String>(200, "Product created successfully", null),
                                HttpStatus.OK);
        }

        /**
         * Deletes an existing product based on the provided product ID.
         *
         * @param id the product ID to delete
         * @return a ResponseEntity containing the status of the deletion operation
         * @throws RequiredArgumentsMissing if the product ID is not provided
         * @throws CustomNotFoundException  if the product is not found
         */
        @Operation(summary = "Delete product by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product deleted successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content) })
        @DeleteMapping("{id}")
        public ResponseEntity<ApiResponseDto<String>> deleteProductById(
                        @Parameter(description = "product id", required = true) @RequestParam(required = true) UUID id)
                        throws RequiredArgumentsMissing, CustomNotFoundException {

                productService.deleteProduct(id);
                return new ResponseEntity<>(
                                new ApiResponseDto<String>(200, "Product deleted successfully", null),
                                HttpStatus.OK);
        }

        /**
         * Updates an existing product based on the provided product ID and the
         * product details.
         *
         * @param ProductDto the product details to update
         * @return a ResponseEntity containing the status of the update operation
         * @throws RequiredArgumentsMissing if the product ID or product details are
         *                                  not provided
         * @throws CustomNotFoundException  if the product is not found
         */
        @Operation(summary = "Update product by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content) })
        @PutMapping("{id}")
        public ResponseEntity<ApiResponseDto<String>> updateProductById(
                        @Parameter(description = "product id", required = true) @RequestBody(required = true) ProductDto ProductDto)
                        throws RequiredArgumentsMissing, CustomNotFoundException {

                productService.updateProduct(ProductDto);
                return new ResponseEntity<>(
                                new ApiResponseDto<String>(200, "Product deleted successfully", null),
                                HttpStatus.OK);
        }
}
