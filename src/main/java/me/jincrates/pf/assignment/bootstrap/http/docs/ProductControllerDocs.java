package me.jincrates.pf.assignment.bootstrap.http.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.jincrates.pf.assignment.application.dto.CreateProductRequest;
import me.jincrates.pf.assignment.application.dto.CreateProductResponse;
import me.jincrates.pf.assignment.application.dto.ProductSummaryResponse;
import me.jincrates.pf.assignment.application.dto.ReviewResponse;
import me.jincrates.pf.assignment.application.dto.UpdateProductRequest;
import me.jincrates.pf.assignment.application.dto.UpdateProductResponse;
import me.jincrates.pf.assignment.bootstrap.http.BaseResponse;
import me.jincrates.pf.assignment.bootstrap.http.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "상품 관리", description = "상품 등록, 수정, 삭제, 조회 API")
public interface ProductControllerDocs {

    @Operation(
        summary = "상품 등록",
        description = "새로운 상품을 등록합니다. 상품명, 판매가격, 할인가격, 브랜드, 카테고리정보를 설정할 수 있습니다."
    )
    ResponseEntity<BaseResponse<CreateProductResponse>> createProduct(
        @Parameter(description = "상품 등록 요청 데이터", required = true)
        @Valid @RequestBody CreateProductRequest request
    );

    @Operation(
        summary = "상품 수정",
        description = "기존 상품 정보를 수정합니다."
    )
    ResponseEntity<BaseResponse<UpdateProductResponse>> updateProduct(
        @Parameter(description = "수정할 상품 ID", required = true, in = ParameterIn.PATH)
        @PathVariable Long productId,
        @Parameter(description = "상품 수정 요청 데이터", required = true)
        @Valid @RequestBody UpdateProductRequest request
    );

    @Operation(
        summary = "상품 삭제",
        description = "상품을 삭제합니다. 해당 상품에 연관된 리뷰 정보도 함께 삭제됩니다."
    )
    ResponseEntity<BaseResponse<Void>> deleteProduct(
        @Parameter(description = "삭제할 상품 ID", required = true, in = ParameterIn.PATH)
        @PathVariable Long productId
    );

    @Operation(
        summary = "카테고리별 상품 목록 조회",
        description = "카테고리 ID를 기준으로 상품 목록을 조회합니다. 페이징과 정렬을 지원합니다."
    )
    ResponseEntity<BaseResponse<PageResponse<ProductSummaryResponse>>> getAllProductsByCategoryId(
        @Parameter(description = "조회할 카테고리 ID", required = true, example = "1")
        @RequestParam Long categoryId,
        @Parameter(
            description = "정렬 기준",
            example = "price_asc",
            schema = @Schema(allowableValues = {"price_asc", "review_desc"})
        )
        @RequestParam(defaultValue = "price_asc") String sort,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지당 아이템 수", example = "5")
        @RequestParam(defaultValue = "5") int size
    );

    @Operation(
        summary = "상품별 리뷰 목록 조회",
        description = "특정 상품의 리뷰 목록을 조회합니다. 최근 등록순으로 정렬됩니다."
    )
    ResponseEntity<BaseResponse<PageResponse<ReviewResponse>>> getAllReviewsByProductId(
        @Parameter(description = "조회할 상품 ID", required = true, in = ParameterIn.PATH)
        @PathVariable Long productId,
        @Parameter(
            description = "정렬 기준",
            example = "created_desc",
            schema = @Schema(allowableValues = {"created_desc", "created_asc"})
        )
        @RequestParam(defaultValue = "created_desc") String sort,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지당 아이템 수", example = "5")
        @RequestParam(defaultValue = "5") int size
    );
}
