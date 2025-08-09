package me.jincrates.pf.assignment.application.repository;

import java.util.List;
import java.util.Optional;
import me.jincrates.pf.assignment.domain.catalog.Category;

public interface CategoryRepository {

    Category save(Category category);

    void saveAll(List<Category> categories);

    Optional<Category> findById(Long categoryId);

    List<Category> findAllByIdIn(List<Long> ids);

    void deleteAllInBatch();
}
