package me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper;

import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ProductJpaEntity;

public class ProductJpaMapper {

    private ProductJpaMapper() {
    }

    public static ProductJpaEntity toEntity(final Product product) {
        ProductJpaEntity entity = ProductJpaEntity.builder()
            .id(product.id())
            .name(product.name())
            .sellingPrice(product.sellingPrice())
            .discountPrice(product.discountPrice())
            .brand(product.brand())
            .build();
        entity.addCategories(CategoryJpaMapper.toEntity(product.categories()));
        return entity;
    }

    public static Product toDomain(final ProductJpaEntity product) {
        return Product.builder()
            .id(product.getId())
            .name(product.getName())
            .sellingPrice(product.getSellingPrice())
            .discountPrice(product.getDiscountPrice())
            .brand(product.getBrand())
            .categories(CategoryJpaMapper.toDomain(product.getCategories()))
            .build();
    }
}
