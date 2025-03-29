package com.testcase.join.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.testcase.join.dto.ProductDto;
import com.testcase.join.dto.ProductFilterDto;
import com.testcase.join.exception.CustomNotFoundException;
import com.testcase.join.exception.RequiredArgumentsMissing;
import com.testcase.join.model.Product;
import com.testcase.join.repository.ProductRepository;

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
     */
    public List<ProductDto> getProducts(ProductFilterDto productFilterDto) {

        Specification<Product> filter = getFilteredProducts(productFilterDto);

        // Paginação
        // Usando o Math.max para garantir que a página não seja negativa
        Pageable pageable = PageRequest.of(Math.max(productFilterDto.page() - 1, 0), productFilterDto.pageSize());

        Page<Product> products = productRepository.findAll(filter, pageable);

        // Convertendo Page para uma lista de ProductDto
        return products.stream().map(product -> new ProductDto(
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

    public Specification<Product> getFilteredProducts(ProductFilterDto productFilterDto) {

        /**
         * Cria uma especificação para filtrar os produtos
         * baseado nos critérios fornecidos
         * A Specification permite criar consultas dinâmicas
         * usando os critérios especificados no ProductFilterDto
         */
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (productFilterDto.name() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + productFilterDto.name().toLowerCase() + "%"));
            }

            if (productFilterDto.category() != null) {
                predicates.add(cb.equal(root.get("category"), productFilterDto.category()));
            }

            if (productFilterDto.brand() != null) {
                predicates.add(cb.equal(root.get("brand"), productFilterDto.brand()));
            }

            if (productFilterDto.minPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), productFilterDto.minPrice()));
            }

            if (productFilterDto.maxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), productFilterDto.maxPrice()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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
}
