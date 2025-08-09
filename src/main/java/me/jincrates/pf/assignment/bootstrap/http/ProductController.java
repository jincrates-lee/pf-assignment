package me.jincrates.pf.assignment.bootstrap.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jincrates.pf.assignment.application.ProductUseCase;
import me.jincrates.pf.assignment.application.dto.CreateProductRequest;
import me.jincrates.pf.assignment.application.dto.CreateProductResponse;
import me.jincrates.pf.assignment.application.dto.UpdateProductRequest;
import me.jincrates.pf.assignment.application.dto.UpdateProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
class ProductController extends BaseController {

    private final ProductUseCase useCase;

    @PostMapping
    public ResponseEntity<BaseResponse<CreateProductResponse>> createProduct(
        @Valid @RequestBody CreateProductRequest request
    ) {
        return created(useCase.create(request));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<BaseResponse<UpdateProductResponse>> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        return ok(useCase.update(request.withId(productId)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse<Void>> deleteProduct(
        @PathVariable Long productId
    ) {
        useCase.delete(productId);
        return noContent();
    }
}
