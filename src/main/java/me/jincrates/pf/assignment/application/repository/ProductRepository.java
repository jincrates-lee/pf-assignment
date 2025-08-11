package me.jincrates.pf.assignment.application.repository;

import java.util.List;
import java.util.Optional;
import me.jincrates.pf.assignment.domain.model.Product;
import me.jincrates.pf.assignment.domain.vo.PageSize;

public interface ProductRepository {

    Product save(Product product);

    Product update(Product newProduct);

    boolean existsById(Long productId);

    Optional<Product> findById(Long productId);

    void deleteById(Long productId);

    List<Product> findAllByCategoryId(
        Long categoryId,
        String sort,
        PageSize pageSize
    );
}
