package com.testcase.join.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.testcase.join.dto.ProductDto;
import com.testcase.join.dto.ProductFilterDto;
import com.testcase.join.exception.RequiredArgumentsMissing;
import com.testcase.join.model.Product;
import com.testcase.join.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the UUID of the product to be retrieved
     * @return a ProductDto containing the product details, or null if not found
     * @throws RequiredArgumentsMissing
     */

    public Product getProduct(UUID id) throws RequiredArgumentsMissing {
        if (id == null) {
            throw new RequiredArgumentsMissing("Product id is missing");
        }
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    public List<ProductDto> getProducts(ProductFilterDto productFilterDto) {
        List<Product> products = productRepository.findAll();

        // Convertendo a lista de Product para uma lista de ProductDto
        return products.stream()
                .map(product -> new ProductDto(Optional.of(product.getId()), product.getName(), Optional.of(product.getDescription()),
                        product.getBrand(), product.getCategory(), product.getQuantity(), product.getPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a page of products based on the provided page number and size.
     * 
     * @param page the page number (0-indexed)
     * @param size the page size
     * @return a Page containing the products or an empty page if no products are
     *         found
     */
    public Page<Product> getProductsPagenated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable);
    }

    /**
     * Creates a new product based on the provided product DTO.
     *
     * @param productDto the product details
     * @throws RequiredArgumentsMissing if the product DTO is null
     */
    public void createProduct(ProductDto productDto) throws RequiredArgumentsMissing {
        if (productDto == null) {
            throw new RequiredArgumentsMissing("Product is missing");
        }

        Product product = Product.builder().id(UUID.randomUUID()).brand(productDto.brand())
                .category(productDto.category()).description(productDto.description().get()).name(productDto.name())
                .price(productDto.price()).quantity(productDto.quantity()).build();
        productRepository.save(product);
    }

    /**
     * Updates an existing product based on the provided product DTO.
     *
     * @param productDto the product details with an existing product ID
     * @throws RequiredArgumentsMissing if the product ID is not present in the
     *                                  product DTO
     */

    public void updateProduct(ProductDto productDto) throws RequiredArgumentsMissing {
        if (!productDto.id().isPresent()) {
            throw new RequiredArgumentsMissing("Product id is missing");
        }
        productRepository.findById(productDto.id().get()).ifPresent(product -> {
            product.setBrand(productDto.brand());
            product.setCategory(productDto.category());
            product.setDescription(productDto.description().get());
            product.setName(productDto.name());
            product.setPrice(productDto.price());
            product.setQuantity(productDto.quantity());
            productRepository.save(product);
        });
    }

    /**
     * Deletes an existing product based on the provided product ID.
     * 
     * @param id the product ID
     * @throws RequiredArgumentsMissing if the product ID is not present
     */
    public void deleteProduct(UUID id) throws RequiredArgumentsMissing {
        if (id == null) {
            throw new RequiredArgumentsMissing("Product id is missing");
        }
        productRepository.findById(id).ifPresent(product -> {
            productRepository.deleteById(id);
        });
    }
}
