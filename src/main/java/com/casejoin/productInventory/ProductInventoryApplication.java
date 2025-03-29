package com.casejoin.productInventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Product Inventory API", version = "1.0", description = "Product Inventory API"))
public class ProductInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductInventoryApplication.class, args);
	}
}
