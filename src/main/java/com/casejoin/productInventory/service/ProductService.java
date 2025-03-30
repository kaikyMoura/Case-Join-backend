package com.casejoin.productInventory.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.casejoin.productInventory.dto.ProductDto;
import com.casejoin.productInventory.dto.ProductFilterDto;
import com.casejoin.productInventory.exception.CustomNotFoundException;
import com.casejoin.productInventory.exception.RequiredArgumentsMissing;
import com.casejoin.productInventory.model.Product;
import com.casejoin.productInventory.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;

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
     * @throws NotFoundException
     */

    public ProductDto getProduct(UUID id) throws RequiredArgumentsMissing, CustomNotFoundException {
        if (id == null) {
            throw new RequiredArgumentsMissing("Product id is missing");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Product not found"));

        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getBrand(),
                product.getCategory(), product.getQuantity(), product.getPrice(), product.getCreatedAt(),
                product.getUpdatedAt());
    }

    /**
     * Retrieves a list of products based on the provided filter criteria.
     *
     * @param productFilterDto the product filter criteria
     * @return a list of ProductDto containing the filtered products
     * @throws CustomNotFoundException if no products are found
     */
    public List<ProductDto> getProducts(ProductFilterDto productFilterDto) throws CustomNotFoundException {
        Specification<Product> specification = isFilterEmpty(productFilterDto) ? null : getFilteredProducts(productFilterDto);

        // Pagination
        // Using Math.max to ensure that the page is at least 1
        // Using ternary operator to ensure that the pageSize is at least 1
        Pageable pageable = PageRequest.of(Math.max(productFilterDto.page() - 1, 0),
                (productFilterDto.pageSize() > 0 ? productFilterDto.pageSize() : 10));

        Page<Product> productsPageable = productRepository.findAll(specification != null ? specification : Specification.where(null), pageable);

        if (productsPageable.isEmpty()) {
            throw new CustomNotFoundException("No products found");
        }

        // Convert to ProductDto
        return productsPageable.stream().map(product -> new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getCategory(),
                product.getQuantity(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a specification to filter products based on the provided filter
     * criteria.
     * This allows for dynamic query creation using the criteria specified in the
     * ProductFilterDto.
     *
     * @param productFilterDto the product filter criteria
     * @return a Specification for filtering products
     */
    private Specification<Product> getFilteredProducts(ProductFilterDto filterDto) {

        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (filterDto.name() != null && !filterDto.name().isBlank()) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + filterDto.name() + "%"));
            }

            if (filterDto.category() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("category"), filterDto.category()));
            }

            if (filterDto.brand() != null && !filterDto.brand().isBlank()) {
                predicate = builder.and(predicate, builder.like(root.get("brand"), "%" + filterDto.brand() + "%"));
            }

            if (filterDto.minPrice() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("price"), filterDto.minPrice()));
            }

            if (filterDto.maxPrice() != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("price"), filterDto.maxPrice()));
            }

            return predicate;
        };
    }

    /**
     * Creates a new product based on the provided product DTO.
     *
     * @param productDto the product details
     * @throws RequiredArgumentsMissing if the product DTO is null
     */
    public void createProduct(ProductDto productDto) throws RequiredArgumentsMissing {
        if (productDto == null || productDto.name() == null || productDto.category() == null
                || productDto.price() == null) {
            throw new RequiredArgumentsMissing("Product is missing or some fields are empty");
        }

        Product product = Product.builder().id(UUID.randomUUID()).brand(productDto.brand())
                .category(productDto.category()).description(productDto.description()).name(productDto.name())
                .price(productDto.price()).quantity(productDto.quantity()).build();
        productRepository.save(product);
    }

    /**
     * Updates an existing product based on the provided product DTO.
     *
     * @param productDto the product details with an existing product ID
     * @throws RequiredArgumentsMissing if the product Id is not present
     * @throws CustomNotFoundException  if the product is not found
     */

    public void updateProduct(ProductDto productDto) throws RequiredArgumentsMissing {
        if (productDto.id() == null) {
            throw new RequiredArgumentsMissing("Product id is missing");
        }

        productRepository.findById(productDto.id()).ifPresentOrElse(product -> {
            product.setBrand(productDto.brand());
            product.setCategory(productDto.category());
            product.setDescription(productDto.description());
            product.setName(productDto.name());
            product.setPrice(productDto.price());
            product.setQuantity(productDto.quantity());
            productRepository.save(product);
        }, () -> {
            new CustomNotFoundException("Product not found");
        });
    }

    /**
     * Deletes an existing product based on the provided product ID.
     * 
     * @param id the product ID
     * @throws RequiredArgumentsMissing if the product Id is not present
     * @throws CustomNotFoundException  if the product is not found
     */
    public void deleteProduct(UUID id) throws RequiredArgumentsMissing, CustomNotFoundException {
        if (id == null) {
            throw new RequiredArgumentsMissing("Product id is missing");
        }
        productRepository.findById(id).ifPresentOrElse(product -> productRepository.delete(product),
                () -> {
                    new CustomNotFoundException("Product not found");
                });
        productRepository.deleteById(id);
    }

    /**
     * Checks if the given product filter is empty.
     * <p>
     * A filter is considered empty if it is null, or if all of its fields are
     * null, empty or blank.
     * 
     * @param filter the filter to check
     * @return true if the filter is empty, false otherwise
     */
    private boolean isFilterEmpty(ProductFilterDto filter) {
        return filter == null || (
            (filter.name() == null || filter.name().isBlank()) &&
            filter.category() == null &&
            (filter.brand() == null || filter.brand().isBlank()) &&
            filter.minPrice() == null &&
            filter.maxPrice() == null
        );
    }
}
