package me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder
@Table(name = "categories")
@Comment("카테고리")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryJpaEntity extends BaseEntity {

    @Column(nullable = false)
    @Comment("카테고리 이름")
    private String name;

    @Setter
    @Column(nullable = false)
    @Comment("카테고리 단계")
    private Integer depth;  // 1, 2, 3...

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryJpaEntity parent; // 자기참조 관계(계층 구조)

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("depth ASC")
    private List<CategoryJpaEntity> children = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private Set<ProductCategoryJpaEntity> productCategories = new HashSet<>();

    public boolean isRoot() {
        return parent == null && depth == 1;
    }

    public void addChild(CategoryJpaEntity child) {
        child.setParent(this);
        child.setDepth(this.depth + 1);
        children.add(child);
    }

    public List<CategoryJpaEntity> getAncestors() {
        List<CategoryJpaEntity> ancestors = new ArrayList<>();
        CategoryJpaEntity current = this.parent;
        while (current != null) {
            ancestors.add(
                0,
                current
            );  // 역순으로 추가
            current = current.getParent();
        }
        return ancestors;
    }

    public List<CategoryJpaEntity> getDescendants() {
        List<CategoryJpaEntity> descendants = new ArrayList<>();
        collectDescendants(
            this,
            descendants
        );
        return descendants;
    }

    public String getFullPath() {
        List<CategoryJpaEntity> ancestors = getAncestors();
        ancestors.add(this);
        return ancestors.stream()
            .map(CategoryJpaEntity::getName)
            .collect(Collectors.joining(">"));
    }

    private void collectDescendants(
        CategoryJpaEntity category,
        List<CategoryJpaEntity> result
    ) {
        for (CategoryJpaEntity child : category.getChildren()) {
            result.add(child);
            collectDescendants(
                child,
                result
            );
        }
    }
}
