package me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder
@Table(name = "products", indexes = {
    // TODO: 인덱스 추가하기
})
@Comment("상품")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductJpaEntity extends BaseEntity {

    @Column(nullable = false)
    @Comment("상품 이름")
    private String name;

    @Column(nullable = false)
    @Comment("상품 원가")
    private BigDecimal originPrice;

    @Column(nullable = false)
    @Comment("상품 판매가")
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    @Comment("상품 할인가")
    private BigDecimal discountPrice;

    @Column(nullable = false)
    @Comment("브랜드 이름")
    private String brandName;

    // 상품-카테고리 다대다 관계
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCategoryJpaEntity> productCategories = new HashSet<>();

    @Version
    private long version;

    public void addCategory(CategoryJpaEntity category) {
        ProductCategoryJpaEntity productCategory = new ProductCategoryJpaEntity(
            this,
            category
        );
        productCategories.add(productCategory);
    }

    public void removeCategory(CategoryJpaEntity category) {
        productCategories.removeIf(it -> it.getCategory().equals(category));
    }

    public List<CategoryJpaEntity> getCategories() {
        return productCategories.stream()
            .map(ProductCategoryJpaEntity::getCategory)
            .toList();
    }
}
