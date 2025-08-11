package me.jincrates.pf.assignment.application;

import java.util.List;
import me.jincrates.pf.assignment.application.dto.CreateProductRequest;
import me.jincrates.pf.assignment.application.dto.CreateProductResponse;
import me.jincrates.pf.assignment.application.dto.GetAllProductsQuery;
import me.jincrates.pf.assignment.application.dto.ProductSummaryResponse;
import me.jincrates.pf.assignment.application.dto.UpdateProductRequest;
import me.jincrates.pf.assignment.application.dto.UpdateProductResponse;

public interface ProductUseCase {

    CreateProductResponse create(CreateProductRequest request);

    UpdateProductResponse update(UpdateProductRequest request);

    void delete(Long productId);

    List<ProductSummaryResponse> getAllProductsByCategoryId(GetAllProductsQuery query);
}
