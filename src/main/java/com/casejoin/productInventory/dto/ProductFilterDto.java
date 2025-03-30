package com.casejoin.productInventory.dto;

import com.casejoin.productInventory.enums.Category;

public record ProductFilterDto(
                String name,
                Category category,
                String brand,
                Double minPrice,
                Double maxPrice,
                Integer page,
                Integer pageSize) {
        public ProductFilterDto() {
                this(null, null, null, null, null, null, null);
        }
}
