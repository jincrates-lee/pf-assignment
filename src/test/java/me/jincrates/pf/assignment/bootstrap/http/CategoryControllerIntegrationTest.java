package me.jincrates.pf.assignment.bootstrap.http;

import static org.assertj.core.api.Assertions.assertThat;

import me.jincrates.pf.assignment.IntegrationTestSupport;
import me.jincrates.pf.assignment.application.dto.CreateCategoryRequest;
import me.jincrates.pf.assignment.application.dto.CreateCategoryResponse;
import me.jincrates.pf.assignment.application.repository.CategoryRepository;
import me.jincrates.pf.assignment.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

@Tag("integration")
@DisplayName("카테고리 API 통합 테스트")
class CategoryControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category parentCategory;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAllInBatch();
        parentCategory = categoryRepository.save(
            Category.builder()
                .name("category-parent")
                .depth(1)
                .build()
        );
    }

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class CreateCategoryTests {

        @Test
        @DisplayName("상위 카테고리 없이 카테고리를 생성하면 1단계 루트 카테고리가 생성된다")
        void createRootCategoryWhenNoParentProvided() {
            // given
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("category-1")
                .build();

            // when
            webTestClient.post()
                .uri("/api/categories")
                .bodyValue(request)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<BaseResponse<CreateCategoryResponse>>() {
                })
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.success()).isTrue();
                    assertThat(response.message()).isBlank();
                    assertThat(response.data()).isNotNull();

                    CreateCategoryResponse data = response.data();
                    assertThat(data).isNotNull();
                    assertThat(data.categoryId()).isNotNull();
                });
        }

        @Test
        @DisplayName("상위 카테고리를 지정하여 카테고리를 생성하면 하위 카테고리가 생성된다")
        void createChildCategoryWhenParentCategoryExists() {
            // given
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("category-child")
                .parentId(parentCategory.id())
                .build();

            // when
            webTestClient.post()
                .uri("/api/categories")
                .bodyValue(request)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<BaseResponse<CreateCategoryResponse>>() {
                })
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.success()).isTrue();
                    assertThat(response.message()).isBlank();
                    assertThat(response.data()).isNotNull();

                    CreateCategoryResponse data = response.data();
                    assertThat(data).isNotNull();
                    assertThat(data.categoryId()).isNotNull();
                });
        }

        @Test
        @DisplayName("존재하지 않는 상위 카테고리를 지정하면 카테고리 생성이 실패한다")
        void failToCreateCategoryWhenParentCategoryNotExists() {
            // given
            Long nonExistentParentId = 99999L;
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("category-child")
                .parentId(nonExistentParentId)
                .build();

            // when
            webTestClient.post()
                .uri("/api/categories")
                .bodyValue(request)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody(BaseResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.success()).isFalse();
                    assertThat(response.message()).isEqualTo("상위 카테고리를 찾을 수 없습니다.");
                    assertThat(response.data()).isNull();
                });
        }

        @Test
        @DisplayName("카테고리명이 비어있으면 카테고리 생성이 실패한다")
        void failToCreateCategoryWhenNameIsEmpty() {
            // given
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("")
                .build();

            // when
            webTestClient.post()
                .uri("/api/categories")
                .bodyValue(request)
                .exchange()
                // then
                .expectStatus().isBadRequest()
                .expectBody(BaseResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.success()).isFalse();
                    assertThat(response.message()).isEqualTo("카테고리 이름은 필수입니다. 입력된 값: [name=]");
                    assertThat(response.data()).isNull();
                });
        }

        @Test
        @DisplayName("카테고리명이 null이면 카테고리 생성이 실패한다")
        void failToCreateCategoryWhenNameIsNull() {
            // given
            CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name(null)
                .build();

            // when
            webTestClient.post()
                .uri("/api/categories")
                .bodyValue(request)
                .exchange()
                // then
                .expectStatus().isBadRequest().expectBody(BaseResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.success()).isFalse();
                    assertThat(response.message()).isEqualTo("카테고리 이름은 필수입니다. 입력된 값: [name=null]");
                    assertThat(response.data()).isNull();
                });
        }
    }
}
