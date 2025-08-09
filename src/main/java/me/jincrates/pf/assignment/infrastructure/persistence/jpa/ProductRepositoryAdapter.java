package me.jincrates.pf.assignment.infrastructure.persistence.jpa;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.repository.ProductRepository;
import me.jincrates.pf.assignment.domain.exception.BusinessException;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.entity.ProductJpaEntity;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.mapper.ProductJpaMapper;
import me.jincrates.pf.assignment.infrastructure.persistence.jpa.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository repository;

    @Override
    public Product save(final Product product) {
        ProductJpaEntity entity = ProductJpaMapper.toEntity(product);
        ProductJpaEntity saved = repository.save(entity);
        return ProductJpaMapper.toDomain(saved);
    }

    @Override
    public Product update(final Product newProduct) {
        if (newProduct.id() == null) {
            throw new BusinessException("상품 ID가 잘못되었습니다.");
        }
        return save(newProduct);
    }

    @Override
    public boolean existsById(final Long productId) {
        return repository.existsById(productId);
    }

    @Override
    public Optional<Product> findById(final Long productId) {
        return repository.findById(productId)
            .map(ProductJpaMapper::toDomain);
    }

    @Override
    public void deleteById(final Long productId) {
        repository.deleteById(productId);
    }
}
