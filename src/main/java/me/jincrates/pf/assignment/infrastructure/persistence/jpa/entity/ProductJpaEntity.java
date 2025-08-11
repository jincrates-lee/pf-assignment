package me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder
@Table(name = "products")
@Comment("상품")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    private Long id;

    @Column(nullable = false)
    @Comment("상품 이름")
    private String name;

    @Column(nullable = false)
    @Comment("상품 판매가")
    private Long sellingPrice;

    @Column(nullable = false)
    @Comment("상품 할인가")
    private Long discountPrice;

    @Column(nullable = false)
    @Comment("브랜드")
    private String brand;

    // 상품-카테고리 다대다 관계
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCategoryJpaEntity> productCategories = new HashSet<>();

    public void addCategories(List<CategoryJpaEntity> categories) {
        categories.forEach(this::addCategory);
    }

    public void addCategory(CategoryJpaEntity category) {
        ProductCategoryJpaEntity productCategory = new ProductCategoryJpaEntity(
            this,
            category
        );
        productCategories.add(productCategory);
    }

    public List<CategoryJpaEntity> getCategories() {
        return productCategories.stream()
            .map(ProductCategoryJpaEntity::getCategory)
            .toList();
    }
}
