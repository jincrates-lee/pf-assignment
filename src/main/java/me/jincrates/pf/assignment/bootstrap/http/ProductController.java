package me.jincrates.pf.assignment.bootstrap.http;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.ProductUseCase;
import me.jincrates.pf.assignment.application.ReviewUseCase;
import me.jincrates.pf.assignment.application.dto.CreateProductRequest;
import me.jincrates.pf.assignment.application.dto.CreateProductResponse;
import me.jincrates.pf.assignment.application.dto.GetAllProductsQuery;
import me.jincrates.pf.assignment.application.dto.GetAllReviewsQuery;
import me.jincrates.pf.assignment.application.dto.ProductSummaryResponse;
import me.jincrates.pf.assignment.application.dto.ReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateProductRequest;
import me.jincrates.pf.assignment.application.dto.UpdateProductResponse;
import me.jincrates.pf.assignment.bootstrap.http.docs.ProductControllerDocs;
import me.jincrates.pf.assignment.domain.vo.PageSize;
import me.jincrates.pf.assignment.domain.vo.ProductSortType;
import me.jincrates.pf.assignment.domain.vo.ReviewSortType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
class ProductController extends BaseController implements ProductControllerDocs {

    private final ProductUseCase productUseCase;
    private final ReviewUseCase reviewUseCase;

    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<CreateProductResponse>> createProduct(
        @Valid @RequestBody CreateProductRequest request
    ) {
        return created(productUseCase.create(request));
    }

    @Override
    @PatchMapping("/{productId}")
    public ResponseEntity<BaseResponse<UpdateProductResponse>> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        return ok(productUseCase.update(request.withId(productId)));
    }

    @Override
    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse<Void>> deleteProduct(
        @PathVariable Long productId
    ) {
        productUseCase.delete(productId);
        return ok();
    }

    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<ProductSummaryResponse>>> getAllProductsByCategoryId(
        @RequestParam Long categoryId,
        @RequestParam(defaultValue = "price_asc") String sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        List<ProductSummaryResponse> response = productUseCase.getAllProductsByCategoryId(
            GetAllProductsQuery.builder()
                .categoryId(categoryId)
                .sort(ProductSortType.fromValue(sort))
                .pageSize(new PageSize(
                    page,
                    size
                ))
                .build()
        );
        return ok(PageResponse.of(
            page,
            size,
            response
        ));
    }

    @Override
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<BaseResponse<PageResponse<ReviewResponse>>> getAllReviewsByProductId(
        @PathVariable Long productId,
        @RequestParam(defaultValue = "created_desc") String sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        List<ReviewResponse> response = reviewUseCase.getAllReviewsByProductId(
            GetAllReviewsQuery.builder()
                .productId(productId)
                .sort(ReviewSortType.fromValue(sort))
                .pageSize(new PageSize(
                    page,
                    size
                ))
                .build()
        );
        return ok(PageResponse.of(
            page,
            size,
            response
        ));
    }
}
