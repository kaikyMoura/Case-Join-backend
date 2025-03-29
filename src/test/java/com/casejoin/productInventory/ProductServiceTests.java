package com.casejoin.productInventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import com.casejoin.productInventory.dto.ProductDto;
import com.casejoin.productInventory.enums.Category;
import com.casejoin.productInventory.exception.CustomNotFoundException;
import com.casejoin.productInventory.exception.RequiredArgumentsMissing;
import com.casejoin.productInventory.model.Product;
import com.casejoin.productInventory.repository.ProductRepository;
import com.casejoin.productInventory.service.ProductService;

class ProductServiceTests {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		product = new Product(UUID.randomUUID(), "Test Product", "Description", "Brand", Category.GAMES, 1, 500.0, null,
				null);
	}

	@Test
	@Description("Should create a new product")
	public void createProductTest() throws RequiredArgumentsMissing {
		productService.createProduct(new ProductDto(null, product.getName(), product.getDescription(),
				product.getBrand(), product.getCategory(), product.getQuantity(), product.getPrice(),
				null, null));

		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	@Description("Should return a product by id")
	void returnProductByIdTest() throws RequiredArgumentsMissing, CustomNotFoundException {
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

		ProductDto foundProduct = productService.getProduct(product.getId());

		assertNotNull(foundProduct);
		assertEquals(product.getName(), foundProduct.name());
		verify(productRepository, times(1)).findById(product.getId());
	}
}
